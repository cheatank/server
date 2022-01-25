package com.github.cheatank.server

import org.slf4j.LoggerFactory

/**
 * オプション
 */
object Options {
    val timeLimit: Short = System.getProperty("timeLimit")?.toShortOrNull() ?: 180
    val lifeCount: Byte = System.getProperty("lifeCount")?.toByteOrNull() ?: 2

    fun load() {
        val logger = LoggerFactory.getLogger("Options")
        logger.info("timeLimit: $timeLimit")
        logger.info("lifeCount: $lifeCount")
    }
}
