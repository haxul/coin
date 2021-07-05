package com.haxul.larix.service

import com.haxul.larix.model.Wallet
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.web3j.crypto.Sign

@SpringBootTest
class WalletServiceTest(
    @Autowired
    val walletService: WalletService
) {

    var wallet: Wallet = Wallet()

    @Test
    fun `when call sign() then return signed data`() {
        val signature = walletService.sign(listOf("hello", "world").toString(), wallet)
        Assertions.assertNotNull(signature)
    }

    @Test
    fun `given valid signed data when call verifySignature then return true`() {
        val msg: String = listOf("hello", "world").toString()
        val signature: Sign.SignatureData = walletService.sign(msg, wallet)

        val valid: Boolean = walletService.verifySignature(wallet.publicKey, msg, signature)

        Assertions.assertTrue(valid)
    }

    @Test
    fun `given invalid signed data when call verifySignature then return false`() {
        val msg: String = listOf("hello", "world").toString()
        val signature: Sign.SignatureData = walletService.sign(msg, wallet)

        val badMsg = listOf("hello", "world", "fake").toString()
        val valid: Boolean = walletService.verifySignature(wallet.publicKey, badMsg, signature)

        Assertions.assertFalse(valid)
    }

    @Test
    fun `given not valid signature when call varifySignature then return false`() {
        val msg: String = listOf("hello", "world").toString()
        val anotherWallet = Wallet()
        val wrongSignature: Sign.SignatureData = walletService.sign(msg, anotherWallet)

        val valid: Boolean = walletService.verifySignature(wallet.publicKey, msg, wrongSignature)

        Assertions.assertFalse(valid)
    }
}