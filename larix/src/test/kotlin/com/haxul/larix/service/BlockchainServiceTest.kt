package com.haxul.larix.service

import com.haxul.larix.model.Block
import com.haxul.larix.model.Blockchain
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime


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
        blockchainService.addBlock(blockchain, listOf("hello"))
        blockchainService.addBlock(blockchain, listOf("world"))
        blockchainService.addBlock(blockchain, listOf("!!!"))

        data = "important data"
    }

    @Test
    fun `when blockchain is created its first block is genesis one`() {
        Assertions.assertEquals(Block.GENESIS_BLOCK, blockchain.chain.first())
    }

    @Test
    fun `given data when call addBlock() then last blockchain block contains this data`() {
        blockchainService.addBlock(blockchain, data)
        Assertions.assertEquals(data, blockchain.chain.last().data as String)
    }

    @Test
    fun `given the blockchain does not start with the genesis block when call isValidBlockchain() then returns false`() {
        blockchain.chain[0] = Block(LocalDateTime.now(), "fake", "fake", emptyList<Any>())
        val valid: Boolean = blockchainService.isValidBlockchain(blockchain)
        Assertions.assertFalse(valid)
    }

    @Test
    fun `given the blockchain with changed lastHash reference when call isValidBlockchain() then return false`() {
        val block = blockchain.chain[2]
        blockchain.chain[2] = Block(block.timestamp, "broken", block.hash, block.data)
        val valid: Boolean = blockchainService.isValidBlockchain(blockchain)
        Assertions.assertFalse(valid)
    }

    @Test
    fun `given block with invalid data when call isValidBlockchain then return false`() {
        val block = blockchain.chain[2]
        blockchain.chain[2] = Block(block.timestamp, block.lastHash, block.hash, listOf("broken"))
        val valid: Boolean = blockchainService.isValidBlockchain(blockchain)
        Assertions.assertFalse(valid)
    }

    @Test
    fun `given block with invalid timestamp when call isValidBlockchain then return false`() {
        val block = blockchain.chain[2]
        blockchain.chain[2] = Block(LocalDateTime.now(), block.lastHash, block.hash, block.data)
        val valid: Boolean = blockchainService.isValidBlockchain(blockchain)
        Assertions.assertFalse(valid)
    }

    @Test
    fun `given block with invalid hash when call isValidBlockchain then return false`() {
        val block = blockchain.chain[2]
        blockchain.chain[2] = Block(block.timestamp, block.lastHash, "broken", block.data)
        val valid: Boolean = blockchainService.isValidBlockchain(blockchain)
        Assertions.assertFalse(valid)
    }

    @Test
    fun `given 2 blocks with invalid data when call isValidBlockchain then return false`() {
        val block = blockchain.chain[2]
        blockchain.chain[2] = Block(block.timestamp, block.lastHash, block.hash, listOf("broken"))
        val block2 = blockchain.chain[2]
        blockchain.chain[2] = Block(block2.timestamp, block2.lastHash, block2.hash, listOf("broken"))
        val valid: Boolean = blockchainService.isValidBlockchain(blockchain)
        Assertions.assertFalse(valid)
    }

    @Test
    fun `when call isValidBlockchain then return true`() {
        Assertions.assertTrue(blockchainService.isValidBlockchain(blockchain))
    }
}