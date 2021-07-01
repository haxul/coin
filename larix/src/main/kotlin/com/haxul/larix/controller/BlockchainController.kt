package com.haxul.larix.controller

import com.haxul.larix.pubsub.RedisMessagePublisher
import com.haxul.larix.controller.dto.AddBlockRequest
import com.haxul.larix.model.Block
import com.haxul.larix.model.Blockchain
import com.haxul.larix.service.BlockchainService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class BlockchainController(
    private val blockchainService: BlockchainService,
    private val redisMessagePublisher: RedisMessagePublisher,
) {

    @GetMapping("/blocks")
    fun getBlocks(): List<Block> = Blockchain.STORAGE.chain

    @PostMapping("/blocks")
    fun addBlock(@RequestBody req: AddBlockRequest): List<Block> {
        blockchainService.addBlock(Blockchain.STORAGE, req.data)
        return Blockchain.STORAGE.chain
    }

    @GetMapping("/send")
    fun send(): String {
        redisMessagePublisher.publish(Blockchain.STORAGE)
        return "sent"
    }
}