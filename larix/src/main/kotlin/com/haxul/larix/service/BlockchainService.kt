package com.haxul.larix.service

import com.haxul.larix.model.Block
import com.haxul.larix.model.Blockchain
import org.springframework.stereotype.Service

@Service
class BlockchainService(val blockService: BlockService) {

    fun addBlock(blockchain: Blockchain, data: Any) {
        val minedBlock: Block = blockService.mineBlock(blockchain.chain.last(), data)
        blockchain.chain.add(minedBlock)
    }
}