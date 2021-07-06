package com.haxul.larix.service

import org.springframework.stereotype.Service
import org.web3j.crypto.Sign

@Service
class WalletService {

    fun verifySignature(publicKey: String, msg: String, signature: Sign.SignatureData): Boolean {
        val pubKeyRecovered = Sign.signedMessageToKey(msg.toByteArray(), signature)
        return publicKey == pubKeyRecovered.toString(16)
    }
}