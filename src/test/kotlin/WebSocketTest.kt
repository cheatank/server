
import com.github.cheatank.common.PacketType
import com.github.cheatank.common.PacketVersion
import com.github.cheatank.common.data.EmptyPacketData
import com.github.cheatank.server.application
import io.ktor.application.Application
import io.ktor.server.testing.withTestApplication
import utils.sendPacket
import utils.testReceivePacket
import kotlin.test.Test
import kotlin.test.assertEquals

class WebSocketTest {
    @Test
    fun getVersionTest() {
        withTestApplication(Application::application) {
            handleWebSocketConversation("/") { incoming, outgoing ->
                outgoing.sendPacket(PacketType.GetVersion, EmptyPacketData)
                incoming.testReceivePacket(PacketType.SendVersion) {
                    assertEquals(it.int, PacketVersion)
                }
            }
        }
    }
}
