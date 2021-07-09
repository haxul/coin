package com.haxul.larix.model

import org.web3j.crypto.ECKeyPair
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.*

class Transaction(
    senderWallet: Wallet,
    recipient: String,
    amount: BigDecimal,
    val txId: String = UUID.randomUUID().toString()
) {
    companion object {
        fun getEmptyTxWithId(id: String): Transaction {
            val fakeKeyPair = ECKeyPair(BigInteger.ZERO, BigInteger.ZERO)
            val fakeWallet = Wallet(fakeKeyPair)
            val tx = Transaction(fakeWallet, "fake", BigDecimal.ZERO, id)
            tx.inputMap.clear()
            tx.outputMap.clear()
            return tx
        }
    }

    val outputMap: MutableMap<String, BigDecimal?> = mutableMapOf(
        recipient to amount,
        senderWallet.publicKey to senderWallet.balance.minus(amount)
    )

    val inputMap: MutableMap<String, Any> = mutableMapOf(
        "timestamp" to LocalDateTime.now(),
        "amount" to senderWallet.balance,
        "address" to senderWallet.publicKey,
        "signature" to senderWallet.sign(outputMap.toString())
    )

    override fun equals(other: Any?) = (other is Transaction)
            && txId == other.txId

    override fun hashCode(): Int = txId.hashCode()

    override fun toString() = """
        id: $txId,
        outputMap: $outputMap,
        inputMap: $inputMap
    """.trimIndent()


}



