package com.haxul.larix.model

import com.haxul.larix.common.GenesisData
import java.time.LocalDateTime

class Block(
    val timestamp: LocalDateTime,
    val lastHash: String,
    val hash: String,
    val data: Any,
    val nonce: Int,
    val difficulty:Int
) {
    companion object {
        val GENESIS_BLOCK: Block = Block(
            GenesisData.TIMESTAMP,
            GenesisData.LAST_HASH,
            GenesisData.HASH,
            GenesisData.DATA,
            GenesisData.NONCE,
            GenesisData.DIFFICULTY
        )
    }

    override fun toString(): String = "Block(timestamp:$timestamp,lastHash:$lastHash,hash:$hash,data:$data)"

}