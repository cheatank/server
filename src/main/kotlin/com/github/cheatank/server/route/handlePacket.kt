package com.github.cheatank.server.route

import com.github.cheatank.common.PacketType
import com.github.cheatank.common.RawPacket
import com.github.cheatank.server.route.packet.getVersion
import com.github.cheatank.server.route.packet.joinQueue
import io.ktor.websocket.DefaultWebSocketServerSession

suspend fun DefaultWebSocketServerSession.handlePacket(rawPacket: RawPacket) {
    when (rawPacket.id) {
        PacketType.GetVersion.id -> getVersion()
        PacketType.JoinQueue.id -> joinQueue()
    }
}
