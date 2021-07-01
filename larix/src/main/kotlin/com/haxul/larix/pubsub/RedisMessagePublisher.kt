package com.haxul.larix.pubsub

import com.google.gson.Gson
import com.haxul.larix.model.Blockchain
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.Topic
import org.springframework.stereotype.Component

@Component
class RedisMessagePublisher(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val blockchainTopic: Topic,
    private val gson: Gson
) {

    fun publish(blockchain: Blockchain) {
        val message: String = gson.toJson(blockchain)
        redisTemplate.convertAndSend(blockchainTopic.topic, message)
    }
}