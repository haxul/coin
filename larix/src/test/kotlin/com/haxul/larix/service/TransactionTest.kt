package com.haxul.larix.service

import com.haxul.larix.model.Transaction
import com.haxul.larix.model.Wallet
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.web3j.crypto.Sign
import java.math.BigDecimal


class TransactionTest {

    @Test
    fun `when create a transaction then return correct outputMap`() {
        val wallet = Wallet()
        val recipient = "no matter"
        val transaction = Transaction(wallet, recipient, BigDecimal("100"))

        val senderWalletBalance: BigDecimal? = transaction.outputMap[wallet.publicKey]
        Assertions.assertEquals("900", senderWalletBalance?.toString() ?: "-1000")
    }
}