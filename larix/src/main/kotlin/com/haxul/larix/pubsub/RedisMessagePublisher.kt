package com.haxul.larix.pubsub

import com.google.gson.Gson
import com.haxul.larix.common.PeerConfig
import com.haxul.larix.model.Blockchain
import com.haxul.larix.model.ChainBroadCastMessage
import com.haxul.larix.model.Transaction
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.Topic
import org.springframework.stereotype.Component

@Component
class RedisMessagePublisher(
    private val redisTemplate: RedisTemplate<String, Any>,
    @Qualifier("blockchainTopic") private val blockchainTopic: Topic,
    @Qualifier("transactionTopic") private val transactionTopic: Topic,
    private val gson: Gson
) {

    fun broadcastBlockchain(blockchain: Blockchain) {
        val body = ChainBroadCastMessage(PeerConfig.PEER_ID, blockchain)
        val message: String = gson.toJson(body)
        redisTemplate.convertAndSend(blockchainTopic.topic, message)
    }

    fun broadcastTransaction(tx: Transaction) {
        redisTemplate.convertAndSend(transactionTopic.topic, gson.toJson(tx))
    }
}