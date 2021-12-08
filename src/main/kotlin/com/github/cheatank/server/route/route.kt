package com.github.cheatank.server.route

import io.ktor.routing.Routing
import io.ktor.websocket.webSocket

/**
 * ルーティングの設定
 */
fun Routing.route() {
    webSocket("/") {
        connectRoute()
    }
}
