package com.haxul.larix.model

import com.haxul.larix.common.WalletConfig
import org.web3j.crypto.ECKeyPair
import java.math.BigDecimal

data class Wallet(
    val keyPair: ECKeyPair,
    val publicKey: String,
    var balance: BigDecimal = WalletConfig.STARTING_BALANCE
)