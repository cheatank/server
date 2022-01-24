package com.github.cheatank.server.utils
import com.github.cheatank.common.Packet
import com.github.cheatank.common.PacketType
import com.github.cheatank.common.RawPacket
import com.github.cheatank.common.data.PacketData
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readBytes
import io.ktor.websocket.DefaultWebSocketServerSession

/**
 * パケットとして取得する
 */
fun Frame.Binary.readPacket(): RawPacket? {
    return Packet.fromByteArray(readBytes())
}

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

/**
 * パケットを送信する。
 * @param packetType
 * @param data
 */
suspend fun <T : PacketData> List<DefaultWebSocketServerSession>.sendPacket(packetType: PacketType<T>, data: T) {
    forEach {
        it.sendPacket(packetType, data)
    }
}

/**
 * セッションを閉じる
 */
suspend fun List<DefaultWebSocketServerSession>.close() {
    forEach {
        it.close()
    }
}
