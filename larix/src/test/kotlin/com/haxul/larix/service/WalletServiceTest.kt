package com.haxul.larix.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class WalletServiceTest(
    @Autowired
    val walletService: WalletService
) {

    @Test
    fun `when call createNewWallet() then returned wallet is not null`() {
        val wallet = walletService.createNewWallet()
        Assertions.assertNotNull(wallet)
    }
}