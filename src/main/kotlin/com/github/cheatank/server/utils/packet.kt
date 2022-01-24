package com.github.cheatank.server.utils
import com.github.cheatank.common.Packet
import com.github.cheatank.common.PacketType
import com.github.cheatank.common.RawPacket
import com.github.cheatank.common.data.PacketData
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readBytes
import io.ktor.websocket.DefaultWebSocketServerSession
import kotlinx.coroutines.channels.ChannelResult

/**
 * パケットとして取得する
 */
fun Frame.Binary.readPacket(): RawPacket? {
    return Packet.fromByteArray(readBytes())
}

/**
 * パケットをバイナリーとして取得する
 * @param data
 */
private fun <T : PacketData> PacketType<T>.asBinary(data: T): Frame.Binary {
    return Frame.Binary(true, Packet.toByteArray(this, data))
}

/**
 * パケットを送信する。
 * @param packetType
 * @param data
 */
suspend fun <T : PacketData> DefaultWebSocketServerSession.sendPacket(packetType: PacketType<T>, data: T) {
    send(packetType.asBinary(data))
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
 * パケットを送信する。
 * @param packetType
 * @param data
 */
fun <T : PacketData> DefaultWebSocketServerSession.trySendPacket(packetType: PacketType<T>, data: T): ChannelResult<Unit> {
    return outgoing.trySend(packetType.asBinary(data))
}

/**
 * パケットを送信する。
 * @param packetType
 * @param data
 */
fun <T : PacketData> List<DefaultWebSocketServerSession>.trySendPacket(packetType: PacketType<T>, data: T): List<ChannelResult<Unit>> {
   return map {
        it.trySendPacket(packetType, data)
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
