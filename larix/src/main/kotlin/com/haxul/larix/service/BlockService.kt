package com.haxul.larix.service

import com.haxul.larix.common.MiningConfig
import com.haxul.larix.model.Block
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class BlockService(val cryptoService: CryptoService) {

    fun mineBlock(lastBlock: Block, data: Any): Block {
        val difficultyValue: Int = if (lastBlock.difficulty <= 1) 1 else lastBlock.difficulty
        var timestamp: LocalDateTime
        var nonce = 0
        var curDifficulty: Int
        var hash: String
        do {
            nonce++
            timestamp = LocalDateTime.now()
            curDifficulty = computeDifficulty(lastBlock, timestamp)
            hash = cryptoService.cryptoHash(
                timestamp.toString(),
                lastBlock.hash,
                data.toString(),
                nonce.toString(),
                curDifficulty.toString()
            )
        } while (!hash.startsWith("0".repeat(difficultyValue)))

        return Block(
            timestamp = timestamp,
            lastHash = lastBlock.hash,
            hash = hash,
            data = data,
            nonce = nonce,
            difficulty = curDifficulty
        )
    }

    fun computeDifficulty(lastBlock: Block, curTime: LocalDateTime): Int {
        val difficulty: Int = lastBlock.difficulty
        if (difficulty <= 1) return 1
        val lastBlockEpoch: Long = lastBlock.timestamp.toEpochSecond(ZoneOffset.UTC)
        val curEpoch: Long = curTime.toEpochSecond(ZoneOffset.UTC)
        return if (lastBlockEpoch + MiningConfig.MINE_RATE > curEpoch) difficulty + 1 else difficulty - 1
    }
}