package com.github.cheatank.server.entity

import kotlin.math.hypot

/**
 * 移動をチェックする
 */
interface LocationChecker {
    /**
     * 移動
     *
     * @return ロールバックするなら座標。しないなら null
     */
    fun moveTo(id: Short, x: Int, y: Int): Pair<Int, Int>?

    /**
     * 何もしない
     */
    object Nothing : LocationChecker {
        override fun moveTo(id: Short, x: Int, y: Int): Pair<Int, Int>? {
            return null
        }
    }

    /**
     * 長距離移動を制限する
     *
     * @param threshold 移動制限の閾値
     */
    class SuppressLongMove(private val threshold: Double) : LocationChecker {
        /**
         * 前回の座標
         */
        private val lastLocations = mutableMapOf<Short, Pair<Int, Int>>()

        override fun moveTo(id: Short, x: Int, y: Int): Pair<Int, Int>? {
            val (lastX, lastY) = lastLocations[id] ?: run {
                lastLocations[id] = x to y
                return null
            }
            val isRollback = threshold < hypot((lastX - x).toDouble(), (lastY - y).toDouble())
            return if (isRollback) {
                lastX to lastY
            } else {
                lastLocations[id] = x to y
                null
            }
        }
    }
}
