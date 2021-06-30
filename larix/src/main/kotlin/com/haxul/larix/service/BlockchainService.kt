package com.haxul.larix.service

import com.haxul.larix.model.Block
import com.haxul.larix.model.Blockchain
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.stereotype.Service

@Service
class BlockchainService(
    val blockService: BlockService,
    val cryptoService: CryptoService
) {

    val logger: Logger = LogManager.getLogger(BlockchainService::class.java)

    fun addBlock(blockchain: Blockchain, data: Any) {
        val minedBlock: Block = blockService.mineBlock(blockchain.chain.last(), data)
        blockchain.chain.add(minedBlock)
        logger.info("new block is added: {}", minedBlock)
    }

    fun isValidBlockchain(blockchain: Blockchain): Boolean {
        if (blockchain.chain.isEmpty()) return false
        if (blockchain.chain[0] != Block.GENESIS_BLOCK) return false
        for (idx in 1 until blockchain.chain.size) {
            if (blockchain.chain[idx - 1].hash != blockchain.chain[idx].lastHash) return false
            if (cryptoService.cryptoHash(blockchain.chain[idx]) != blockchain.chain[idx].hash) return false
        }
        return true
    }

    fun replaceBlockchain(origin: Blockchain, incoming: Blockchain) {
        if (origin.chain.size >= incoming.chain.size) return
        if (!isValidBlockchain(incoming)) return
        origin.chain = incoming.chain
        logger.info("blockchain is replaced: {}", origin.chain)
    }
}