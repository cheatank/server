package com.github.cheatank.server.route.packet

import com.github.cheatank.server.entity.Lobby
import io.ktor.websocket.DefaultWebSocketServerSession

/**
 * ロビーのキューに追加する
 */
suspend fun DefaultWebSocketServerSession.joinQueue() {
    Lobby.joinQueue(this)?.invoke()
}
