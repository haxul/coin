package com.haxul.larix.service

import com.haxul.larix.common.MiningConfig
import com.haxul.larix.exception.ExceedWalletBalanceTxException
import com.haxul.larix.model.Transaction
import com.haxul.larix.model.TransactionPool
import com.haxul.larix.model.Wallet
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.web3j.crypto.Sign
import java.math.BigDecimal

class TransactionServiceTest {

    private val transactionService = TransactionService(WalletService())
    private lateinit var senderWallet: Wallet
    private lateinit var tx: Transaction

    private val recipientAddress = "recipientAddress"
    private val recipientAmount = BigDecimal(50)

    private lateinit var validTxs: MutableList<Transaction>

    @BeforeEach
    fun beforeEach() {
        senderWallet = Wallet()
        tx = Transaction(
            amount = recipientAmount,
            recipient = recipientAddress,
            senderWallet = senderWallet
        )

        TransactionPool.txMap.clear()
        validTxs = mutableListOf()
        for (i in 1 until 30) {
            val tx = Transaction(senderWallet, "any-recipient", BigDecimal(30))
            if (i % 3 == 0) tx.inputMap["amount"] = BigDecimal(9999999)
            else validTxs.add(tx)
            TransactionPool.add(tx)
        }
    }


    @Test
    fun `when call getValidTxs then return valid txs`() {
        val validTxsFromPool = transactionService.getValidTxsFromPool()
        assertEquals(validTxs.size, validTxsFromPool.size)
    }

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
        assertEquals(senderWallet.publicKey, newTx.inputMap["address"])
    }

    @Test
    fun `when call createTx() then tx output recipient matches amount`() {
        val recipient = "foo"
        val amount = BigDecimal(100)
        val newTx = transactionService.createTx(amount, recipient, senderWallet)

        assertEquals(amount, newTx.outputMap[recipient])
    }

    @Test
    fun `when update tx then return correct amount`() {
        val newRecipient = "newAddress"
        val newAmount = BigDecimal(100)
        transactionService.updateTx(senderWallet, newRecipient, newAmount, tx)

        assertEquals(newAmount, tx.outputMap[newRecipient])
    }

    @Test
    fun `when update tx then extract money from senderWallet`() {
        val originSenderOutput: BigDecimal? = tx.outputMap[senderWallet.publicKey]
        val newRecipient = "newAddress"
        val newAmount = BigDecimal(100)
        transactionService.updateTx(senderWallet, newRecipient, newAmount, tx)

        assertEquals(originSenderOutput!! - newAmount, tx.outputMap[senderWallet.publicKey])
    }

    @Test
    fun `when update tx then debit and credit must match each other`() {
        val newRecipient = "newAddress"
        val newAmount = BigDecimal(100)
        transactionService.updateTx(senderWallet, newRecipient, newAmount, tx)

        val sum: BigDecimal? = tx.outputMap.values.reduce { acc, v -> acc?.plus(v!!) }

        assertEquals(tx.inputMap["amount"], sum)
    }

    @Test
    fun `when update tx then signature must be recomputed`() {
        val originSignature: Sign.SignatureData = tx.inputMap["signature"] as Sign.SignatureData
        val newAmount = BigDecimal(100)
        transactionService.updateTx(senderWallet, "no matter", newAmount, tx)
        Assertions.assertNotEquals(originSignature, tx.inputMap["signature"] as Sign.SignatureData)
    }

    @Test
    fun `given amount exceeds wallet balance when call updateTx then throws exception`() {
        Assertions.assertThrows(ExceedWalletBalanceTxException::class.java)
        { transactionService.updateTx(senderWallet, "no matter", BigDecimal(9999999999), tx) }
    }

    @Test
    fun `given two recipient with the same address when call update tx then outMap should increase its money`() {
        val newAmount = BigDecimal(100)
        transactionService.updateTx(senderWallet, recipientAddress, newAmount, tx)
        assertEquals(newAmount + recipientAmount, tx.outputMap[recipientAddress])
    }

    @Test
    fun `when call createRewardTx then return tx to reward a miner`() {
        val minerWallet = Wallet()
        val rewardTx: Transaction = transactionService.createRewardTx(minerWallet)
        assertEquals(rewardTx.inputMap["address"], MiningConfig.REWARD_BLOCKCHAIN_ADDRESS)
        assertEquals(MiningConfig.MINING_REWARD, rewardTx.outputMap[minerWallet.publicKey])
    }
}