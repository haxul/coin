package com.haxul.larix.common

import java.time.LocalDateTime
import java.time.Month

object GenesisData {
    val TIMESTAMP: LocalDateTime = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0, 0)
    val LAST_HASH: String = "------"
    val HASH: String = "hash-one"
    val DATA = emptyList<Any>()
}