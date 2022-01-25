package com.github.cheatank.server.entity

import com.github.cheatank.common.PacketType
import com.github.cheatank.common.data.GameData
import com.github.cheatank.common.data.LocationData
import com.github.cheatank.common.data.SelfLocationData
import com.github.cheatank.common.data.ShortData
import com.github.cheatank.server.Options.lifeCount
import com.github.cheatank.server.Options.longDistanceThreshold
import com.github.cheatank.server.Options.suppressCheat
import com.github.cheatank.server.Options.timeLimit
import com.github.cheatank.server.utils.close
import com.github.cheatank.server.utils.readPacket
import com.github.cheatank.server.utils.sendPacket
import com.github.cheatank.server.utils.trySendPacket
import io.ktor.http.cio.websocket.Frame
import io.ktor.websocket.DefaultWebSocketServerSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger

/**
 * ゲーム
 */
data class Game(
    private val sessions: List<DefaultWebSocketServerSession>,
) {
    private val logger = LoggerFactory.getLogger("Game@${hashCode()}")
    private val sessionById: Map<Short, DefaultWebSocketServerSession>
    private var time = timeLimit
    private val locationChecker = if (suppressCheat) LocationChecker.SuppressLongMove(longDistanceThreshold) else LocationChecker.Nothing

    init {
        val id = AtomicInteger(0)
        sessionById = sessions.associateBy { id.getAndIncrement().toShort() }
    }

    /**
     * ゲームを開始する
     */
    suspend fun start() {
        sessionById.forEach { (id, session) ->
            session.sendPacket(PacketType.StartGame, GameData(id, lifeCount, timeLimit))
        }
        sendInitialLocation()
        withContext(Dispatchers.Unconfined) {
            awaitAll(
                async { countdown() },
                *sessionById.map { (id, session) ->
                    async {
                        for (frame in session.incoming) {
                            if (frame is Frame.Binary) {
                                val packet = frame.readPacket() ?: continue
                                when (packet.id) {
                                    PacketType.UpdateSelfLocation.id -> {
                                        val (x, y, yaw) = packet.toPacket(PacketType.UpdateSelfLocation)?.data as? SelfLocationData ?: continue
                                        locationChecker.moveTo(id, x, y)?.let { (_x, _y) ->
                                            logger.trace("$id rollback: ($x, $y) -> ($_x, $_y)")
                                            sessions.forEach { // rollback
                                                it.sendPacket(PacketType.UpdateLocation, LocationData(id, _x, _y, yaw))
                                            }
                                        } ?: run {
                                            sessions.forEach {
                                                if (it != session) {
                                                    it.sendPacket(PacketType.UpdateLocation, LocationData(id, x, y, yaw))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }.toTypedArray()
            )
        }
    }

    /**
     * 初期位置を送信する
     */
    private suspend fun sendInitialLocation() {
        var x = 100
        sessionById.forEach { (id, _) ->
            val y = 100
            locationChecker.moveTo(id, x, y)
            sessions.sendPacket(PacketType.UpdateLocation, LocationData(id, x, y, 0))
            x += 200
        }
    }

    /**
     * カウントダウンを開始する
     */
    private suspend fun countdown() {
        while (true) {
            delay(1000)
            if (time == 0.toShort()) {
                sessions.sendPacket(PacketType.EndGame, ShortData(-1))
                sessions.close()
                return
            } else {
                time --
                sessions.trySendPacket(PacketType.Countdown, ShortData(time))
            }
        }
    }
}
