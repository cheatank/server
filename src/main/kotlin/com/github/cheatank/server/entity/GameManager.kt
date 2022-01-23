package com.github.cheatank.server.entity

import io.ktor.websocket.DefaultWebSocketServerSession

/**
 * ゲームを管理する
 */
object GameManager {
    private val games = mutableListOf<Game>()

    suspend fun start(sessions: List<DefaultWebSocketServerSession>) {
        Game(sessions).apply(games::add).start()
    }
}
