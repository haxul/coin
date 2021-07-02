package com.haxul.larix.pubsub

import com.google.gson.Gson
import com.haxul.larix.common.PeerConfig
import com.haxul.larix.model.Blockchain
import com.haxul.larix.model.ChainBroadCastMessage
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
        val incomingMessage: ChainBroadCastMessage =
            gson.fromJson(message.toString(), ChainBroadCastMessage::class.java)
        if (incomingMessage.peerId == PeerConfig.PEER_ID) return
        val incomingBlockchain = incomingMessage.blockchain
        val isReplaced: Boolean = blockchainService.replaceBlockchain(Blockchain.STORAGE, incomingBlockchain)
        if (isReplaced) {
            Blockchain.STORAGE.chain = CopyOnWriteArrayList(incomingBlockchain.chain)
            logger.info("the blockchain is synchronized with ${incomingMessage.peerId}")
            return
        }
        logger.info("incoming blockchain is behind")
    }
}