package com.haxul.larix.service

import com.haxul.larix.model.Block
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class BlockService(val cryptoService: CryptoService) {

    fun mineBlock(lastBlock: Block, data: Any): Block {
        var timestamp: LocalDateTime
        var nonce = 0
        val difficulty = lastBlock.difficulty
        var hash = ""
        do {
            nonce++
            timestamp = LocalDateTime.now()
            hash = cryptoService.cryptoHash(
                timestamp.toString(),
                lastBlock.hash,
                data.toString(),
                nonce.toString(),
                difficulty.toString()
            )
        } while (!hash.startsWith("0".repeat(difficulty)))


        return Block(
            timestamp = timestamp,
            lastHash = lastBlock.hash,
            hash = hash,
            data = data,
            nonce = nonce,
            difficulty = difficulty
        )
    }
}