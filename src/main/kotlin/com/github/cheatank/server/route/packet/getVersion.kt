package com.github.cheatank.server.route.packet

import com.github.cheatank.common.PacketType
import com.github.cheatank.common.PacketVersion
import com.github.cheatank.common.data.IntData
import com.github.cheatank.server.utils.sendPacket
import io.ktor.websocket.DefaultWebSocketServerSession

suspend fun DefaultWebSocketServerSession.getVersion() {
    sendPacket(PacketType.SendVersion, IntData(PacketVersion))
}
