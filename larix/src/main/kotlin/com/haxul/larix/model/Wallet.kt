package com.haxul.larix.model

import com.haxul.larix.common.WalletConfig
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Keys
import java.math.BigDecimal

class Wallet {
    val keyPair: ECKeyPair = Keys.createEcKeyPair()
    val publicKey: String = keyPair.publicKey.toString(16)
    var balance: BigDecimal = WalletConfig.STARTING_BALANCE
}