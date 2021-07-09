package com.haxul.larix.controller

import com.haxul.larix.controller.dto.AddBlockRequest
import com.haxul.larix.controller.dto.TransactCreateRequest
import com.haxul.larix.model.*
import com.haxul.larix.pubsub.RedisMessagePublisher
import com.haxul.larix.service.BlockchainService
import com.haxul.larix.service.TransactionService
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/api")
class BlockchainController(
    private val blockchainService: BlockchainService,
    private val redisMessagePublisher: RedisMessagePublisher,
    private val transactionService: TransactionService
) {

    @GetMapping("/blocks")
    fun getBlocks(): List<Block> = Blockchain.STORAGE.chain

    @PostMapping("/blocks")
    fun addBlock(@RequestBody req: AddBlockRequest): List<Block> {
        blockchainService.addBlock(Blockchain.STORAGE, req.data)
        redisMessagePublisher.broadcastBlockchain(Blockchain.STORAGE)
        return Blockchain.STORAGE.chain
    }

    @PostMapping("/transact")
    fun transact(@RequestBody req: TransactCreateRequest): Transaction {
        val existingTx: Transaction? = TransactionPool.existingTx(Wallet.NODE_WALLET.publicKey)
        existingTx?.let {
            transactionService.updateTx(Wallet.NODE_WALLET, req.recipient, req.amount, existingTx)
            redisMessagePublisher.broadcastTransaction(existingTx)
            return existingTx
        }
        val newTx: Transaction = transactionService.createTx(req.amount, req.recipient, Wallet.NODE_WALLET)
        TransactionPool.add(newTx)
        redisMessagePublisher.broadcastTransaction(newTx)
        return newTx
    }

    @GetMapping("/tx-pool")
    fun getTxPool() = TransactionPool.txMap
}