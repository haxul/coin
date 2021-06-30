package com.haxul.larix.service

import com.haxul.larix.common.GenesisData
import com.haxul.larix.model.Block
import org.junit.jupiter.api.Assertions
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

    val lastBlock: Block = Block(LocalDateTime.now(), "helloworld", "world", emptyList<Any>(), GenesisData.NONCE, GenesisData.DIFFICULTY)
    val data = listOf("Hello", "World")

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
}