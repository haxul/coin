package com.haxul.larix.service

import com.haxul.larix.model.Wallet
import org.springframework.stereotype.Service
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Keys

@Service
class WalletService {

    fun createNewWallet(): Wallet {
        val keyPair: ECKeyPair = Keys.createEcKeyPair()
        return Wallet(keyPair, keyPair.publicKey.toString(16))
    }
}