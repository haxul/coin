package com.haxul.larix.service

import com.haxul.larix.exception.ExceedWalletBalanceTxException
import com.haxul.larix.model.Transaction
import com.haxul.larix.model.Wallet
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.stereotype.Service
import org.web3j.crypto.Sign
import java.lang.RuntimeException
import java.math.BigDecimal

@Service
class TransactionService(
    private val walletService: WalletService
) {

    val logger: Logger = LogManager.getLogger(TransactionService::class.java)

    fun createTx(amount: BigDecimal, recipient: String, wallet: Wallet): Transaction {
        if (wallet.balance < amount) throw ExceedWalletBalanceTxException("wallet has no money to create tx")
        return Transaction(wallet, recipient, amount)
    }

    fun isTxValid(tx: Transaction): Boolean {
        val outputTotal: BigDecimal = tx.outputMap.values.reduce { total, outputAmount -> total.add(outputAmount) }
        if (outputTotal != tx.inputMap["amount"]) {
            logger.error("invalid tx: ${tx.txId}")
            return false
        }

        val valid = walletService.verifySignature(
            tx.inputMap["address"] as String,
            tx.outputMap.toString(),
            tx.inputMap["signature"] as Sign.SignatureData
        )
        if (!valid) {
            logger.error("invalid signature in tx ${tx.txId}")
            return false
        }

        return true
    }
}