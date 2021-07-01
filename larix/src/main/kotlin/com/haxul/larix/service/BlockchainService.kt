package com.haxul.larix.service

import com.haxul.larix.model.Block
import com.haxul.larix.model.Blockchain
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.stereotype.Service
import kotlin.math.abs

@Service
class BlockchainService(
    val blockService: BlockService,
    val cryptoService: CryptoService
) {

    val logger: Logger = LogManager.getLogger(BlockchainService::class.java)

    fun addBlock(blockchain: Blockchain, data: List<String>) {
        val minedBlock: Block = blockService.mineBlock(blockchain.chain.last(), data)
        blockchain.chain.add(minedBlock)
        logger.info("new block is added: {}", minedBlock)
    }

    fun isValidBlockchain(blockchain: Blockchain): Boolean {
        if (blockchain.chain.isEmpty()) return false
        if (blockchain.chain[0] != Block.GENESIS_BLOCK) return false
        for (idx in 1 until blockchain.chain.size) {
            val prev: Block = blockchain.chain[idx - 1]
            val cur: Block = blockchain.chain[idx]
            if (prev.hash != cur.lastHash) return false
            if (cryptoService.cryptoHash(cur) != cur.hash) return false
            if (abs(cur.difficulty - prev.difficulty) > 1) return false
        }
        return true
    }

    fun replaceBlockchain(origin: Blockchain, incoming: Blockchain): Boolean {
        if (origin.chain.size >= incoming.chain.size) return false
        if (!isValidBlockchain(incoming)) return false
        origin.chain = incoming.chain
        logger.info("blockchain is replaced: {}", origin.chain)
        return true
    }
}