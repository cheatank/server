package com.github.cheatank.server.entity

import io.ktor.websocket.DefaultWebSocketServerSession
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * ロビー
 */
object Lobby {
    private val logger = LoggerFactory.getLogger("Lobby")
    private val queue = ConcurrentLinkedQueue<LobbyPlayer>()
    private val mutex = Mutex()

    /**
     * キューに追加する
     */
    suspend fun joinQueue(session: DefaultWebSocketServerSession): (suspend () -> Unit)? {
        mutex.withLock {
            while (true) {
                if (queue.isEmpty()) break
                val player = queue.remove()
                if (player.isClosed) continue
                return {
                    logger.info("Game start: ${session.hashCode()} & ${player.session.hashCode()}")
                    GameManager.start(listOf(session, player.session))
                }
            }
            logger.info("Add Queue: ${session.hashCode()}")
            queue.add(LobbyPlayer(session))
            return null
        }
    }
}
