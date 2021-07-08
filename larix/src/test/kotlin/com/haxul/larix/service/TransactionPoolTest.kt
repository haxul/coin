package com.haxul.larix.service

import com.haxul.larix.model.Transaction
import com.haxul.larix.model.TransactionPool
import com.haxul.larix.model.Wallet
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class TransactionPoolTest {

    private val tx = Transaction(Wallet(), "randonAddress", BigDecimal(100))

    @BeforeEach
    fun beforeEach() {
        TransactionPool.txMap.clear()
    }

    @Test
    fun `when call transaction pool setTx then tx is placed in transaction pool`() {
        TransactionPool.add(tx)

        Assertions.assertEquals(tx, TransactionPool.find(tx.txId))
    }
}