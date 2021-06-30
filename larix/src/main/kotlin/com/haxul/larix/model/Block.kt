package com.haxul.larix.model

import com.haxul.larix.common.GenesisData
import java.time.LocalDateTime

class Block(
    val timestamp: LocalDateTime,
    val lastHash: String,
    val hash: String,
    val data: Any,
    val nonce: Int,
    var difficulty: Int
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

    override fun equals(other: Any?) = (other is Block)
            && timestamp == other.timestamp
            && lastHash == other.lastHash
            && hash == other.hash
            && data == other.data
            && nonce == other.nonce
            && difficulty == other.difficulty

    override fun hashCode(): Int {
        var result = timestamp.hashCode()
        result = 31 * result + lastHash.hashCode()
        result = 31 * result + hash.hashCode()
        result = 31 * result + data.hashCode()
        result = 31 * result + nonce
        result = 31 * result + difficulty
        return result
    }
}