package com.haxul.larix.model

import com.haxul.larix.common.WalletConfig
import java.math.BigDecimal

data class Wallet(
    val publicKey: String,
    var balance: BigDecimal = WalletConfig.STARTING_BALANCE
)