package com.haxul.larix.service

import com.haxul.larix.model.Block
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class BlockServiceTest(
    @Autowired
    val blockService: BlockService
) {

    val lastBlock: Block = Block(LocalDateTime.now(), "helloworld", "world", emptyList<Any>())
    val data = listOf("Hello", "World")

    @Test
    fun `given lastBlock and data when call mineblock() then return correct lastHash`() {
        val newBlock = blockService.mineBlock(lastBlock, data)
        Assertions.assertEquals(lastBlock.hash, newBlock.lastHash)
    }

    @Test
    fun `when call mineBlock() then data is correct`() {
        val newBlock = blockService.mineBlock(lastBlock, data)
        Assertions.assertArrayEquals(arrayOf(data), arrayOf(newBlock.data))
    }
}