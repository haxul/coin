package com.haxul.larix.service

import org.springframework.stereotype.Service
import java.security.MessageDigest

@Service
class CryptoService {

    fun cryptoHash(vararg els: String): String {
        els.sort()
        val originString = els.joinToString(separator = "")
        val digest = MessageDigest.getInstance("SHA-256")
        val encodedHash: ByteArray = digest.digest(originString.encodeToByteArray())
        val hexString: StringBuilder = StringBuilder(2 * encodedHash.size)
        for (element in encodedHash) {
            val hex = Integer.toHexString(0xff and element.toInt())
            if (hex.length == 1) hexString.append('0')
            hexString.append(hex)
        }
        return hexString.toString()
    }
}