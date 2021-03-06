package com.haxul.larix.model

object TransactionPool {
    var txMap: MutableMap<String, Transaction> = mutableMapOf()

    fun add(tx: Transaction) {
        txMap[tx.txId] = tx
    }

    fun find(id: String): Transaction? = txMap[id]

    fun existingTx(inputAddress: String): Transaction? {
        txMap.forEach {
            val senderAddress: String = it.value.inputMap["address"] as String
            if (senderAddress == inputAddress) return it.value
        }
        return null
    }

    fun clear() = txMap.clear()

}