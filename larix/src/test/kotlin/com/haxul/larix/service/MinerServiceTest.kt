package com.haxul.larix.service

import com.haxul.larix.model.Transaction
import com.haxul.larix.model.TransactionPool
import com.haxul.larix.model.Wallet
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest
class MinerServiceTest(
    @Autowired
    private val minerService: MinerService
) {

}