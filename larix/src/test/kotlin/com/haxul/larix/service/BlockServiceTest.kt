package com.haxul.larix.service

import com.haxul.larix.common.GenesisData
import com.haxul.larix.common.PeerConfig
import com.haxul.larix.model.Block
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class BlockServiceTest(
    @Autowired
    val blockService: BlockService,
    @Autowired
    val cryptoService: CryptoService
) {

    var lastBlock: Block =
        Block(LocalDateTime.now(), "helloworld", "world", emptyList<String>(), GenesisData.NONCE, 3)
    var data = listOf("Hello", "World")

    @Test
    fun `given lastBlock and data when call mineBlock() then return correct lastHash`() {
        val newBlock = blockService.mineBlock(lastBlock, data)
        assertEquals(lastBlock.hash, newBlock.lastHash)
    }

    @Test
    fun `when call mineBlock() then data is correct`() {
        val newBlock = blockService.mineBlock(lastBlock, data)
        assertArrayEquals(arrayOf(data), arrayOf(newBlock.data))
    }

    @Test
    fun `when call mineBlock() then new block has correct hash`() {
        val newBlock: Block = blockService.mineBlock(lastBlock, data)
        assertEquals(
            cryptoService.cryptoHash(
                newBlock.timestamp.toString(),
                lastBlock.hash,
                data.toString(),
                newBlock.nonce.toString(),
                newBlock.difficulty.toString()
            ), newBlock.hash
        )
    }

    @Test
    fun `when call mineBlock then it fits mining criteria`() {
        val minedBlock: Block = blockService.mineBlock(lastBlock, data)
        assertTrue(minedBlock.hash.startsWith("0".repeat(GenesisData.DIFFICULTY)))
    }

    @Test
    fun `when compare two identical genesis block then they are equal`() {
        val block = Block(
            Block.GENESIS_BLOCK.timestamp,
            Block.GENESIS_BLOCK.lastHash,
            Block.GENESIS_BLOCK.hash,
            Block.GENESIS_BLOCK.data,
            Block.GENESIS_BLOCK.nonce,
            Block.GENESIS_BLOCK.difficulty
        )

        assertTrue(block == Block.GENESIS_BLOCK)
    }

    @Test
    fun `given quickly mined block when call adjustDifficulty then raise the difficulty`() {
        val computed = blockService
            .computeDifficulty(lastBlock, LocalDateTime.now().plusSeconds((PeerConfig.MINE_RATE + 3).toLong()))
        assertEquals(lastBlock.difficulty - 1, computed)
    }

    @Test
    fun `given slow mined block when call adjustDifficulty then lower the difficulty`() {
        val computed = blockService
            .computeDifficulty(lastBlock, LocalDateTime.now().plusNanos(50000))
        assertEquals(lastBlock.difficulty + 1, computed)
    }

    @Test
    fun `when call mineBlock() then returned block has correct difficulty`() {
        val minedBlock = blockService.mineBlock(lastBlock, data)
        val valid =
            minedBlock.difficulty == lastBlock.difficulty + 1 || minedBlock.difficulty == lastBlock.difficulty - 1
        assertTrue(valid)
    }

    @Test
    fun `given block with negative difficulty when call mineBlock() then returned block has lower difficulty which equals 1`() {
        lastBlock.difficulty = -1
        val block: Block = blockService.mineBlock(lastBlock, data)
        assertEquals(1, block.difficulty)
    }
}