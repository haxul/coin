package com.haxul.larix.pubsub

import com.google.gson.Gson
import com.haxul.larix.common.PeerConfig
import com.haxul.larix.model.Blockchain
import com.haxul.larix.model.ChainBroadCastMessage
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.Topic
import org.springframework.stereotype.Component

@Component
class RedisMessagePublisher(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val blockchainTopic: Topic,
    private val gson: Gson
) {

    fun broadcastBlockchain(blockchain: Blockchain) {
        val body = ChainBroadCastMessage(PeerConfig.PEER_ID, blockchain)
        val message: String = gson.toJson(body)
        redisTemplate.convertAndSend(blockchainTopic.topic, message)
    }
}