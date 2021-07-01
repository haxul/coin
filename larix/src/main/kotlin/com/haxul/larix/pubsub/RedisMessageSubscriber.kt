package com.haxul.larix.pubsub

import com.google.gson.Gson
import com.haxul.larix.model.Blockchain
import com.haxul.larix.service.BlockchainService
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.stereotype.Component
import java.util.concurrent.CopyOnWriteArrayList
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Component
class RedisMessageSubscriber(
    private val gson: Gson,
    private val blockchainService: BlockchainService
) : MessageListener {

    val logger: Logger = LogManager.getLogger(RedisMessageSubscriber::class.java)

    override fun onMessage(message: Message, pattern: ByteArray?) {
        val incomingBlockchain = gson.fromJson(message.toString(), Blockchain::class.java)
        val isReplaced: Boolean = blockchainService.replaceBlockchain(Blockchain.STORAGE, incomingBlockchain)
        if (isReplaced) {
            Blockchain.STORAGE.chain = CopyOnWriteArrayList(incomingBlockchain.chain)
            logger.info("the blockchain is synchronized")
            return
        }
        logger.info("incoming blockchain is behind")
    }
}