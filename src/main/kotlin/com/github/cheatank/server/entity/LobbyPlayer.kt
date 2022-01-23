package com.github.cheatank.server.entity

import io.ktor.websocket.DefaultWebSocketServerSession
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * ロビーで待機するプレイヤー
 */
data class LobbyPlayer(
    val session: DefaultWebSocketServerSession,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    val isClosed
        get() = session.outgoing.isClosedForSend
}
