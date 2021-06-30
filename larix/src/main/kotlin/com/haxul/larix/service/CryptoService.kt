package com.haxul.larix.service

import com.haxul.larix.model.Block
import org.springframework.stereotype.Service
import java.security.MessageDigest

@Service
class CryptoService {

    fun cryptoHash(vararg els: String): String {
        els.sort()
        val originString: String = els.joinToString(separator = "")
        val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
        val encodedHash: ByteArray = digest.digest(originString.encodeToByteArray())
        val hexSb: StringBuilder = StringBuilder(2 * encodedHash.size)
        for (element in encodedHash) {
            val hex: String = Integer.toHexString(0xff and element.toInt())
            if (hex.length == 1) hexSb.append('0')
            hexSb.append(hex)
        }
        return hexSb.toString()
    }

    fun cryptoHash(block: Block): String =
        cryptoHash(
            block.timestamp.toString(),
            block.lastHash,
            block.data.toString(),
            block.nonce.toString(),
            block.difficulty.toString()
        )
}