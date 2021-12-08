package utils

import com.github.cheatank.common.Packet
import com.github.cheatank.common.PacketType
import com.github.cheatank.common.data.PacketData
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readBytes
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.test.assertIs
import kotlin.test.assertNotNull

suspend fun <T : PacketData> ReceiveChannel<Frame>.receivePacket(packetType: PacketType<T>): Packet<T>? {
    return Packet.fromByteArray(receive().readBytes())?.toPacket(packetType)
}

suspend inline fun <reified T : PacketData> ReceiveChannel<Frame>.testReceivePacket(packetType: PacketType<T>, block: (T) -> Unit) {
    val packet = receivePacket(packetType)
    assertNotNull(packet)
    val packetData = packet.data
    assertIs<T>(packetData)
    block(packetData)
}
