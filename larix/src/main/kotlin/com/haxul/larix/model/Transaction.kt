package com.haxul.larix.model

import java.math.BigDecimal
import java.util.*

data class Transaction(
    val senderWallet: Wallet,
    val recipient: String,
    val amount: BigDecimal
) {
    val txId: String = UUID.randomUUID().toString()
    val outputMap: Map<String, BigDecimal> = mapOf(
        recipient to amount,
        senderWallet.publicKey to senderWallet.balance.minus(amount)
    )
}



