package utils

import com.github.cheatank.common.Packet
import com.github.cheatank.common.PacketType
import com.github.cheatank.common.data.PacketData
import io.ktor.http.cio.websocket.Frame
import kotlinx.coroutines.channels.SendChannel

suspend fun <T : PacketData> SendChannel<Frame>.sendPacket(packetType: PacketType<T>, data: T) {
    send(Frame.Binary(true, Packet.toByteArray(packetType, data)))
}
