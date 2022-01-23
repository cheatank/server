package com.github.cheatank.server.entity

import com.github.cheatank.common.PacketType
import com.github.cheatank.common.data.EmptyPacketData
import com.github.cheatank.server.utils.sendPacket
import io.ktor.websocket.DefaultWebSocketServerSession

/**
 * ゲーム
 */
data class Game(
    private val sessions: List<DefaultWebSocketServerSession>,
) {
    suspend fun start() {
        sessions.forEach {
            it.sendPacket(PacketType.StartGame, EmptyPacketData)
        }
    }
}
