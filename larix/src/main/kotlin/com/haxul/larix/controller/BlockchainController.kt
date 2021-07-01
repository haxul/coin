package com.haxul.larix.controller

import com.haxul.larix.config.RedisMessagePublisher
import com.haxul.larix.config.RedisMessageSubscriber
import com.haxul.larix.controller.dto.AddBlockRequest
import com.haxul.larix.model.Block
import com.haxul.larix.service.BlockchainService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class BlockchainController(
    private val blockchainService: BlockchainService,
    private val redisMessagePublisher: RedisMessagePublisher,
) {
    val ledger = blockchainService.ledger

    @GetMapping("/blocks")
    fun getBlocks(): List<Block> = blockchainService.ledger.chain

    @PostMapping("/blocks")
    fun addBlock(@RequestBody req: AddBlockRequest): List<Block> {
        blockchainService.addBlock(blockchainService.ledger, req.data)
        return ledger.chain
    }

    @GetMapping("/send")
    fun test(@RequestBody req: AddBlockRequest):String {
        redisMessagePublisher.publish(req.data.toString())
        return "sent"
    }
}