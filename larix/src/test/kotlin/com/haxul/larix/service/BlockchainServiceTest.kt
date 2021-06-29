package com.haxul.larix.service

import com.haxul.larix.model.Block
import com.haxul.larix.model.Blockchain
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class BlockchainServiceTest(
    @Autowired
    val blockchainService: BlockchainService
) {

    lateinit var blockchain: Blockchain
    lateinit var data: String

    @BeforeEach
    fun setup() {
        blockchain = Blockchain()
        data = "important data"
    }

    @Test
    fun `when blockchain is create its first block is genesis one`() {
        Assertions.assertEquals(Block.GENESIS_BLOCK, blockchain.chain.first())
    }

    @Test
    fun `given data when call addBlock() then last blockchain block contains this data`() {
        blockchainService.addBlock(blockchain, data)

        Assertions.assertEquals(data, blockchain.chain.last().data as String)
    }
}