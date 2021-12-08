package com.github.cheatank.server.utils
import com.github.cheatank.common.Packet
import com.github.cheatank.common.PacketType
import com.github.cheatank.common.data.PacketData
import io.ktor.http.cio.websocket.Frame
import io.ktor.websocket.DefaultWebSocketServerSession

/**
 * パケットを送信する。
 * @param packetType
 * @param data
 */
suspend fun <T : PacketData> DefaultWebSocketServerSession.sendPacket(packetType: PacketType<T>, data: T) {
    val bytes = Packet.toByteArray(packetType, data)
    val frame = Frame.Binary(true, bytes)
    send(frame)
}
