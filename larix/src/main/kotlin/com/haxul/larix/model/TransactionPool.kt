package com.haxul.larix.model

object TransactionPool {
    val txMap: MutableMap<String, Transaction?> = mutableMapOf()

    fun setTx(tx: Transaction) {
        txMap[tx.txId] = tx
    }

    fun getTx(id: String): Transaction? = txMap[id]
}