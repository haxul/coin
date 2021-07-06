package com.haxul.larix.service

import com.haxul.larix.exception.ExceedWalletBalanceTxException
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
        Assertions.assertTrue(transactionService.isTxValid(tx))
    }

    @Test
    fun `given amount exceeds wallet balance when call createTx() then throws error`() {
        val exceedBalance: BigDecimal = senderWallet.balance.plus(BigDecimal(200))
        Assertions.assertThrows(ExceedWalletBalanceTxException::class.java) {
            transactionService.createTx(exceedBalance, "foo", senderWallet)
        }
    }

    @Test
    fun `when call createTx() then tx input matches with the wallet`() {
        val newTx = transactionService.createTx(BigDecimal(50), "foo", senderWallet)
        Assertions.assertEquals(senderWallet.publicKey, newTx.inputMap["address"])
    }

    @Test
    fun `when call createTx() then tx output recipient matches amount`() {
        val recipient = "foo"
        val amount = BigDecimal(100)
        val newTx = transactionService.createTx(amount, recipient, senderWallet)

        Assertions.assertEquals(amount, newTx.outputMap[recipient])
    }
}