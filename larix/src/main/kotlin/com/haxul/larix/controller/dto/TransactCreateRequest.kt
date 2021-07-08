package com.haxul.larix.controller.dto

import java.math.BigDecimal

data class TransactCreateRequest(
    val amount: BigDecimal,
    val recipient: String
)