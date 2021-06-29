package com.haxul.larix.service

import com.haxul.larix.model.Block
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class BlockService(val cryptoService: CryptoService) {

    fun mineBlock(lastBlock: Block, data: Any): Block {
        val timestamp = LocalDateTime.now()
        val hash: String = cryptoService
            .cryptoHash(timestamp.toString(), lastBlock.hash, data.toString())

        return Block(
            timestamp = timestamp,
            lastHash = lastBlock.hash,
            hash = hash,
            data = data
        )
    }
}