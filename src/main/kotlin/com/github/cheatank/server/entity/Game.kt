package com.github.cheatank.server.entity

import com.github.cheatank.common.PacketType
import com.github.cheatank.common.data.ConfigData
import com.github.cheatank.common.data.EmptyPacketData
import com.github.cheatank.common.data.ShortData
import com.github.cheatank.server.utils.sendPacket
import io.ktor.websocket.DefaultWebSocketServerSession
import kotlinx.coroutines.delay

/**
 * ゲーム
 */
data class Game(
    private val sessions: List<DefaultWebSocketServerSession>,
) {
    private val timeLimit: Short = 3 * 60
    private var time = timeLimit

    /**
     * ゲームを開始する
     */
    suspend fun start() {
        sessions.sendPacket(PacketType.StartGame, ConfigData(2, timeLimit))
        countdown()
    }

    /**
     * カウントダウンを開始する
     */
    private suspend fun countdown() {
        while (true) {
            delay(1000)
            if (time == 0.toShort()) {
                sessions.sendPacket(PacketType.EndGame, EmptyPacketData)
                return
            } else {
                time --
                sessions.sendPacket(PacketType.Countdown, ShortData(time))
            }
        }
    }
}
