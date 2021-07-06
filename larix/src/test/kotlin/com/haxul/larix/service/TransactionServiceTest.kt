package com.haxul.larix.service

import com.haxul.larix.model.Transaction
import com.haxul.larix.model.Wallet
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class TransactionServiceTest {

    private val transactionService = TransactionService(WalletService())
    private val senderWallet = Wallet()
    private val tx = Transaction(
        amount = BigDecimal("50"),
        recipient = "recipientAddress",
        senderWallet = senderWallet
    )

    @Test
    fun `when call isTransactionValid() then return true`() {
        Assertions.assertTrue(transactionService.isTransactionValid(tx))
    }
}