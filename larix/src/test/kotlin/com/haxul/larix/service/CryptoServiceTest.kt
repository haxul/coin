package com.haxul.larix.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CryptoServiceTest(
    @Autowired
    val cryptoService: CryptoService
) {

    @Test
    fun `when call cryptoHash() then return string of hash`() {
        val cryptoHash: String = cryptoService.cryptoHash("hello", "world")
        assertEquals("936a185caaa266bb9cbe981e9e05cb78cd732b0b3280eb944412bb6f8f8f07af", cryptoHash)
    }

    @Test
    fun `given string in different order when call cryptoHash() then two hash are equal`() {
        val h1: String = cryptoService.cryptoHash("hello", "world")
        val h2: String = cryptoService.cryptoHash("world", "hello")
        assertEquals(h1, h2)
        assertEquals("936a185caaa266bb9cbe981e9e05cb78cd732b0b3280eb944412bb6f8f8f07af", h1)
    }
}