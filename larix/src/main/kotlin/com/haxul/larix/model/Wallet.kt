package com.haxul.larix.model

import com.haxul.larix.common.WalletConfig
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Hash
import org.web3j.crypto.Keys
import org.web3j.crypto.Sign
import java.math.BigDecimal

class Wallet {

    val keyPair: ECKeyPair = Keys.createEcKeyPair()
    val publicKey: String = keyPair.publicKey.toString(16)
    var balance: BigDecimal = WalletConfig.STARTING_BALANCE


    fun sign(msg: String): Sign.SignatureData {
        val msgHash = Hash.sha3(msg.toByteArray())
        return Sign.signMessage(msgHash, keyPair, false)
    }
}