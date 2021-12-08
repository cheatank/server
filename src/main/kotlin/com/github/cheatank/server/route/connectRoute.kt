package com.github.cheatank.server.route

import com.github.cheatank.server.utils.readPacket
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.websocket.DefaultWebSocketServerSession

suspend fun DefaultWebSocketServerSession.connectRoute() {
    for (frame in incoming) {
        when (frame) {
            is Frame.Binary ->
                (frame.readPacket())?.let {
                    processPacket(it)
                } ?: continue
            is Frame.Close -> close()
        }
    }
}
