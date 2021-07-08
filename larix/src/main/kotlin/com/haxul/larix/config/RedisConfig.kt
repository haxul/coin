package com.haxul.larix.config

import com.haxul.larix.pubsub.RedisMessageBlockchainSubscriber
import com.haxul.larix.pubsub.RedisMessageTransactionSubscriber
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import org.springframework.data.redis.serializer.GenericToStringSerializer


@Configuration
class RedisConfig(
    private val redisMessageBlockchainSubscriber: RedisMessageBlockchainSubscriber,
    private val redisMessageTransactionSubscriber: RedisMessageTransactionSubscriber
) {
    @Bean
    fun container(
        redisConnectionFactory: RedisConnectionFactory,
        @Qualifier("messageBlockchainListenerAdapter") blockchainListerAdapter: MessageListenerAdapter,
        @Qualifier("messageTransactionListenerAdapter") txListenerAdapter: MessageListenerAdapter
    ): RedisMessageListenerContainer {
        val redisMessageListenerContainer = RedisMessageListenerContainer()
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory)
        redisMessageListenerContainer.addMessageListener(blockchainListerAdapter, blockchainTopic())
        redisMessageListenerContainer.addMessageListener(txListenerAdapter, transactionTopic())
        return redisMessageListenerContainer
    }

    @Bean
    fun messageBlockchainListenerAdapter(): MessageListenerAdapter =
        MessageListenerAdapter(redisMessageBlockchainSubscriber, "onMessage")

    @Bean
    fun messageTransactionListenerAdapter(): MessageListenerAdapter =
        MessageListenerAdapter(redisMessageTransactionSubscriber, "onMessage")

    @Bean
    fun blockchainTopic(): ChannelTopic = ChannelTopic("BLOCKCHAIN")

    @Bean
    fun transactionTopic(): ChannelTopic = ChannelTopic("TRANSACTION")

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.setConnectionFactory(redisConnectionFactory)
        redisTemplate.valueSerializer = GenericToStringSerializer(Any::class.java)
        return redisTemplate
    }
}