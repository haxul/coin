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
        fun getGenesisBlock(): Block {
            return Block(
                GenesisData.timestamp,
                GenesisData.lastHash,
                GenesisData.hash,
                GenesisData.data
            )
        }
    }
}