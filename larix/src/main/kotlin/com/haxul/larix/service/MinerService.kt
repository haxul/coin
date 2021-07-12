package com.haxul.larix.service

import com.haxul.larix.model.Wallet
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.stereotype.Service

@Service
class MinerService(
    private val transactionService: TransactionService
) {

    val logger: Logger = LogManager.getLogger(MinerService::class.java)

    fun mineTx(wallet: Wallet) {
        //get the tx pool valid transactions

        // generate miner's reward
        // add block consisting txs to the blockchain
        // broadcast the blockchain
        // clear tx pool
    }

}