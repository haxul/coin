package com.haxul.larix.config

import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.stereotype.Component

@Component
class RedisMessageSubscriber : MessageListener {

    override fun onMessage(message: Message, pattern: ByteArray?) {
        println("got message ${message}")
    }
}