package com.haxul.larix.service

import com.haxul.larix.model.Block
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class BlockService {

    fun mineBlock(lastBlock: Block, data: Any): Block = Block(
        timestamp = LocalDateTime.now(),
        lastHash = lastBlock.hash,
        hash = "TODO",
        data = data
    )
}