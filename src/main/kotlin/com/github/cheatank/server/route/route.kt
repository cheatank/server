package com.github.cheatank.server.route

import com.github.cheatank.server.utils.readPacket
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.routing.Routing
import io.ktor.websocket.webSocket

/**
 * ルーティングの設定
 */
fun Routing.route() {
    webSocket("/") {
        for (frame in incoming) {
            when (frame) {
                is Frame.Binary -> {
                    val packet = frame.readPacket() ?: continue
                    handlePacket(packet)
                }
                is Frame.Close -> {
                    close()
                }
            }
        }
    }
}
