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
    val blockchainService: BlockchainService,
    @Autowired
    val cryptoService: CryptoService
) {

    lateinit var blockchain: Blockchain
    lateinit var data: List<String>
    lateinit var anotherBlockchain: Blockchain

    @BeforeEach
    fun setup() {
        blockchain = Blockchain()
        blockchainService.addBlock(blockchain, listOf("hello"))
        blockchainService.addBlock(blockchain, listOf("world"))
        blockchainService.addBlock(blockchain, listOf("!!!"))

        anotherBlockchain = Blockchain()

        data = listOf("important data")
    }

    @Test
    fun `when blockchain is created its first block is genesis one`() {
        Assertions.assertEquals(Block.GENESIS_BLOCK, blockchain.chain.first())
    }

    @Test
    fun `given data when call addBlock() then last blockchain block contains this data`() {
        blockchainService.addBlock(blockchain, data)
        Assertions.assertEquals(data[0], blockchain.chain.last().data[0])
    }

    @Test
    fun `given the blockchain does not start with the genesis block when call isValidBlockchain() then returns false`() {
        blockchain.chain[0] = Block(LocalDateTime.now(), "fake", "fake", emptyList(), 0, 0)
        val valid: Boolean = blockchainService.isValidBlockchain(blockchain)
        Assertions.assertFalse(valid)
    }

    @Test
    fun `given the blockchain with changed lastHash reference when call isValidBlockchain() then return false`() {
        val block = blockchain.chain[2]
        blockchain.chain[2] = Block(block.timestamp, "broken", block.hash, block.data, block.nonce, block.difficulty)
        val valid: Boolean = blockchainService.isValidBlockchain(blockchain)
        Assertions.assertFalse(valid)
    }

    @Test
    fun `given block with invalid data when call isValidBlockchain then return false`() {
        val block = blockchain.chain[2]
        blockchain.chain[2] =
            Block(block.timestamp, block.lastHash, block.hash, listOf("broken"), block.nonce, block.difficulty)
        val valid: Boolean = blockchainService.isValidBlockchain(blockchain)
        Assertions.assertFalse(valid)
    }

    @Test
    fun `given block with invalid timestamp when call isValidBlockchain then return false`() {
        val block = blockchain.chain[2]
        blockchain.chain[2] =
            Block(LocalDateTime.now(), block.lastHash, block.hash, block.data, block.nonce, block.difficulty)
        val valid: Boolean = blockchainService.isValidBlockchain(blockchain)
        Assertions.assertFalse(valid)
    }

    @Test
    fun `given the chain contains a block with jumped difficulty when call isValidBlockchain then returns false`() {
        val timestamp = LocalDateTime.now()
        val lastHash: String = blockchain.chain.last().hash
        val data = listOf<String>()
        val nonce = 1
        val difficulty: Int = blockchain.chain.last().difficulty - 3
        val cryptoHash = cryptoService.cryptoHash(
            timestamp.toString(),
            lastHash,
            data.toString(),
            nonce.toString(),
            difficulty.toString()
        )
        val badBlock = Block(timestamp, lastHash, cryptoHash, data, nonce, difficulty)
        blockchain.chain.add(badBlock)

        val validBlockchain = blockchainService.isValidBlockchain(blockchain)
        Assertions.assertFalse(validBlockchain)
    }

    @Test
    fun `given block with invalid hash when call isValidBlockchain then return false`() {
        val block = blockchain.chain[2]
        blockchain.chain[2] =
            Block(block.timestamp, block.lastHash, "broken", block.data, block.nonce, block.difficulty)
        val valid: Boolean = blockchainService.isValidBlockchain(blockchain)
        Assertions.assertFalse(valid)
    }

    @Test
    fun `given 2 blocks with invalid data when call isValidBlockchain then return false`() {
        val block = blockchain.chain[2]
        blockchain.chain[2] =
            Block(block.timestamp, block.lastHash, block.hash, listOf("broken"), block.nonce, block.difficulty)
        val block2 = blockchain.chain[2]
        blockchain.chain[2] =
            Block(block2.timestamp, block2.lastHash, block2.hash, listOf("broken"), block.nonce, block.difficulty)
        val valid: Boolean = blockchainService.isValidBlockchain(blockchain)
        Assertions.assertFalse(valid)
    }

    @Test
    fun `when call isValidBlockchain then return true`() {
        Assertions.assertTrue(blockchainService.isValidBlockchain(blockchain))
    }

    @Test
    fun `given anotherBlockchain is not longer than current one when call replaceBlockchain then blockchain is not replaced `() {
        blockchainService.replaceBlockchain(blockchain, anotherBlockchain)
        Assertions.assertEquals(4, blockchain.chain.size)
        Assertions.assertEquals("!!!", (blockchain.chain[3].data as List<*>)[0] as String)
    }


    @Test
    fun `given not valid anotherBlockchain is longer than current one when call replaceBlockchain then blockchain is not replaced`() {
        blockchainService.addBlock(anotherBlockchain, listOf("hello"))
        blockchainService.addBlock(anotherBlockchain, listOf("world"))
        blockchainService.addBlock(anotherBlockchain, listOf("!!!"))
        blockchainService.addBlock(anotherBlockchain, listOf("???"))
        val block: Block = anotherBlockchain.chain[3]
        anotherBlockchain.chain[3] =
            Block(block.timestamp, block.lastHash, "fake", block.data, block.nonce, block.difficulty)

        blockchainService.replaceBlockchain(blockchain, anotherBlockchain)
        Assertions.assertEquals(4, blockchain.chain.size)
        Assertions.assertEquals("!!!", (blockchain.chain[3].data as List<*>)[0] as String)
    }

    @Test
    fun `given not valid anotherBlockchain is longer than current one and has wrong data in a block when call replaceBlockchain then blockchain is not replaced`() {
        blockchainService.addBlock(anotherBlockchain, listOf("hello"))
        blockchainService.addBlock(anotherBlockchain, listOf("world"))
        blockchainService.addBlock(anotherBlockchain, listOf("!!!"))
        blockchainService.addBlock(anotherBlockchain, listOf("???"))
        val block: Block = anotherBlockchain.chain[3]
        anotherBlockchain.chain[2] =
            Block(block.timestamp, block.lastHash, block.hash, listOf("fake"), block.nonce, block.difficulty)

        blockchainService.replaceBlockchain(blockchain, anotherBlockchain)
        Assertions.assertEquals(4, blockchain.chain.size)
        Assertions.assertEquals("!!!", (blockchain.chain.last().data as List<*>)[0] as String)
    }

    @Test
    fun `given valid anotherBlockchain is longer than current one when call replaceBlockchain then blockchain is replaced`() {
        blockchainService.addBlock(anotherBlockchain, listOf("hello"))
        blockchainService.addBlock(anotherBlockchain, listOf("world"))
        blockchainService.addBlock(anotherBlockchain, listOf("!!!"))
        blockchainService.addBlock(anotherBlockchain, listOf("???"))

        blockchainService.replaceBlockchain(blockchain, anotherBlockchain)
        Assertions.assertEquals(5, blockchain.chain.size)
        Assertions.assertEquals("???", (blockchain.chain.last().data as List<*>)[0] as String)
    }

    @Test
    fun `given valid anotherBlockchain is ahead than current one for 2 elements when call replaceBlockchain then blockchain is replaced`() {
        blockchainService.addBlock(anotherBlockchain, listOf("hello"))
        blockchainService.addBlock(anotherBlockchain, listOf("world"))
        blockchainService.addBlock(anotherBlockchain, listOf("!!!"))
        blockchainService.addBlock(anotherBlockchain, listOf("???"))
        blockchainService.addBlock(anotherBlockchain, listOf("****"))

        blockchainService.replaceBlockchain(blockchain, anotherBlockchain)
        Assertions.assertEquals(6, blockchain.chain.size)
        Assertions.assertEquals("****", (blockchain.chain.last().data as List<*>)[0] as String)
    }
}