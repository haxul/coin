package com.haxul.larix.config

import com.haxul.larix.pubsub.RedisMessageSubscriber
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
    private val redisMessageSubscriber: RedisMessageSubscriber
) {
    @Bean
    fun container(
        redisConnectionFactory: RedisConnectionFactory,
        messageListenerAdapter: MessageListenerAdapter
    ): RedisMessageListenerContainer {
        val redisMessageListenerContainer = RedisMessageListenerContainer()
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory)
        redisMessageListenerContainer.addMessageListener(messageListenerAdapter, blockchainTopic())
        return redisMessageListenerContainer
    }

    @Bean
    fun messageListenerAdapter(): MessageListenerAdapter = MessageListenerAdapter(redisMessageSubscriber, "onMessage")

    @Bean
    fun blockchainTopic(): ChannelTopic = ChannelTopic("BLOCKCHAIN")

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.setConnectionFactory(redisConnectionFactory)
        redisTemplate.valueSerializer = GenericToStringSerializer(Any::class.java)
        return redisTemplate
    }
}