package com.haxul.larix.service

import com.haxul.larix.model.Wallet
import org.springframework.stereotype.Service
import org.web3j.crypto.Hash
import org.web3j.crypto.Sign


@Service
class WalletService {

    fun verifySignature(pubKey: String, msg: String, signature: Sign.SignatureData): Boolean {
        val pubKeyRecovered = Sign.signedMessageToKey(msg.toByteArray(), signature)
        return pubKey == pubKeyRecovered.toString(16)
    }

    fun sign(msg: String, wallet: Wallet): Sign.SignatureData {
        val msgHash = Hash.sha3(msg.toByteArray())
        return Sign.signMessage(msgHash, wallet.keyPair, false)
    }
}