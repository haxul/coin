package com.haxul.larix.model

import com.haxul.larix.common.GenesisData
import java.time.LocalDateTime

class Block(
    val timestamp: LocalDateTime,
    val lastHash: String,
    val hash: String,
    var data: Any
) {
    companion object {
        val GENESIS_BLOCK: Block = Block(
            GenesisData.TIMESTAMP,
            GenesisData.LAST_HASH,
            GenesisData.HASH,
            GenesisData.DATA
        )
    }

    override fun toString(): String = "Block(timestamp:$timestamp,lastHash:$lastHash,hash:$hash,data:$data)"

}