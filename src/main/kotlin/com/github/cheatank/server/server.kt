package com.github.cheatank.server

import com.github.cheatank.server.route.route
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.http.cio.websocket.pingPeriod
import io.ktor.http.cio.websocket.timeout
import io.ktor.routing.Routing
import io.ktor.routing.routing
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.websocket.WebSockets
import java.time.Duration

/**
 * サーバーを起動する
 */
fun startServer() {
    embeddedServer(CIO, port = 8080, module = Application::application).start(true)
}

/**
 * サーバーの設定をする
 */
fun Application.application() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing(Routing::route)
}
