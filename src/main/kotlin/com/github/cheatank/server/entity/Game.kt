package com.github.cheatank.server.entity

import com.github.cheatank.common.PacketType
import com.github.cheatank.common.data.GameData
import com.github.cheatank.common.data.ShortData
import com.github.cheatank.server.utils.close
import com.github.cheatank.server.utils.sendPacket
import io.ktor.websocket.DefaultWebSocketServerSession
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicInteger

/**
 * ゲーム
 */
data class Game(
    private val sessions: List<DefaultWebSocketServerSession>,
) {
    private val sessionById: Map<Short, DefaultWebSocketServerSession>
    private val idBySession: Map<DefaultWebSocketServerSession, Short>
    private val timeLimit: Short = 3 * 60
    private var time = timeLimit

    init {
        val id = AtomicInteger(0)
        sessionById = sessions.associateBy { id.getAndIncrement().toShort() }
        idBySession = sessionById.entries.associate { (key, value) -> value to key }
    }

    /**
     * ゲームを開始する
     */
    suspend fun start() {
        sessionById.forEach { (id, session) ->
            session.sendPacket(PacketType.StartGame, GameData(id, 2, timeLimit))
        }
        countdown()
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
