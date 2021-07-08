package com.haxul.larix.controller

import com.haxul.larix.controller.dto.AddBlockRequest
import com.haxul.larix.controller.dto.TransactCreateRequest
import com.haxul.larix.model.*
import com.haxul.larix.pubsub.RedisMessagePublisher
import com.haxul.larix.service.BlockchainService
import com.haxul.larix.service.TransactionService
import org.springframework.web.bind.annotation.*

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
        val tx: Transaction = transactionService.createTx(
            recipient = req.recipient,
            amount = req.amount,
            wallet = Wallet.NODE_WALLET
        )
        TransactionPool.setTx(tx)
        return tx
    }

//    @GetMapping("/test")
//    fun get(@RequestBody s: Any): Sign.SignatureData {
//        println(s)
//
//        val linkedHashMap = s as LinkedHashMap<*, *>
//        val signatureData = Sign.SignatureData(
//            (linkedHashMap.get("v") as Int).toByte(), linkedHashMap.get("r").toString().toByteArray(),
//            linkedHashMap.get("s").toString().toByteArray()
//        )
////        val walletService = WalletService()
////        val sign: Sign.SignatureData = walletService.sign("hello", Wallet())
//        return sign
//    }
}