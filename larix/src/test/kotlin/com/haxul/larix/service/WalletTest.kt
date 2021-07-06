package com.haxul.larix.service

import com.haxul.larix.model.Wallet
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.web3j.crypto.Sign

class WalletTest {

    private var walletService = WalletService()
    var wallet: Wallet = Wallet()

    @Test
    fun `when call sign() then return signed data`() {
        val signature = wallet.sign(listOf("hello", "world").toString())
        Assertions.assertNotNull(signature)
    }

    @Test
    fun `given valid signed data when call verifySignature then return true`() {
        val msg: String = listOf("hello", "world").toString()
        val signature: Sign.SignatureData = wallet.sign(msg)

        val valid: Boolean = walletService.verifySignature(wallet.publicKey, msg, signature)

        Assertions.assertTrue(valid)
    }

    @Test
    fun `given invalid signed data when call verifySignature then return false`() {
        val msg: String = listOf("hello", "world").toString()
        val signature: Sign.SignatureData = wallet.sign(msg)

        val badMsg = listOf("hello", "world", "fake").toString()
        val valid: Boolean = walletService.verifySignature(wallet.publicKey, badMsg, signature)
        Assertions.assertFalse(valid)
    }

    @Test
    fun `given not valid signature when call varifySignature then return false`() {
        val msg: String = listOf("hello", "world").toString()
        val anotherWallet = Wallet()
        val wrongSignature: Sign.SignatureData = anotherWallet.sign(msg)
        val valid: Boolean = walletService.verifySignature(wallet.publicKey, msg, wrongSignature)

        Assertions.assertFalse(valid)
    }
}