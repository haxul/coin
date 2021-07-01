package com.haxul.larix.config

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.Topic
import org.springframework.stereotype.Component

@Component
class RedisMessagePublisher(
    val redisTemplate: RedisTemplate<String, Any>,
    val topic: Topic
) {

    fun publish(message: String) {
        redisTemplate.convertAndSend(topic.topic, message)
    }
}