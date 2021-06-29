package com.haxul.larix.common

import java.time.LocalDateTime
import java.time.Month

object GenesisData {
    val timestamp: LocalDateTime = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0, 0)
    val lastHash: String = "------"
    val hash: String = "hash-one"
    val data = emptyList<Any>()
}