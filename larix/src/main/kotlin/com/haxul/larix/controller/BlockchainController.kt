package com.haxul.larix.controller

import com.haxul.larix.controller.dto.AddBlockRequest
import com.haxul.larix.model.Block
import com.haxul.larix.model.Blockchain
import com.haxul.larix.pubsub.RedisMessagePublisher
import com.haxul.larix.service.BlockchainService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class BlockchainController(
    private val blockchainService: BlockchainService,
    private val redisMessagePublisher: RedisMessagePublisher
) {

    @GetMapping("/blocks")
    fun getBlocks(): List<Block> = Blockchain.STORAGE.chain

    @PostMapping("/blocks")
    fun addBlock(@RequestBody req: AddBlockRequest): List<Block> {
        blockchainService.addBlock(Blockchain.STORAGE, req.data)
        redisMessagePublisher.broadcastBlockchain(Blockchain.STORAGE)
        return Blockchain.STORAGE.chain
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