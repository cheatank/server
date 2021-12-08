package com.github.cheatank.server.utils

import com.github.cheatank.common.Packet
import com.github.cheatank.common.RawPacket
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readBytes

fun Frame.Binary.readPacket(): RawPacket? {
    return Packet.fromByteArray(readBytes())
}
