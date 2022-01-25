package com.github.cheatank.server.entity

import com.github.cheatank.common.PacketType
import com.github.cheatank.common.data.GameData
import com.github.cheatank.common.data.LocationData
import com.github.cheatank.common.data.SelfLocationData
import com.github.cheatank.common.data.ShortData
import com.github.cheatank.server.utils.close
import com.github.cheatank.server.utils.readPacket
import com.github.cheatank.server.utils.sendPacket
import io.ktor.http.cio.websocket.Frame
import io.ktor.websocket.DefaultWebSocketServerSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger

/**
 * ゲーム
 */
data class Game(
    private val sessions: List<DefaultWebSocketServerSession>,
) {
    private val sessionById: Map<Short, DefaultWebSocketServerSession>
    private val timeLimit: Short = 3 * 60
    private var time = timeLimit

    init {
        val id = AtomicInteger(0)
        sessionById = sessions.associateBy { id.getAndIncrement().toShort() }
    }

    /**
     * ゲームを開始する
     */
    suspend fun start() {
        sessionById.forEach { (id, session) ->
            session.sendPacket(PacketType.StartGame, GameData(id, 2, timeLimit))
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
            sessions.sendPacket(PacketType.UpdateLocation, LocationData(id, x, 100, 0))
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
                sessions.sendPacket(PacketType.Countdown, ShortData(time))
            }
        }
    }
}
