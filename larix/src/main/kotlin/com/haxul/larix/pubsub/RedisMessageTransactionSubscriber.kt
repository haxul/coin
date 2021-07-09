package com.haxul.larix.pubsub

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.haxul.larix.model.Transaction
import com.haxul.larix.model.TransactionPool
import com.haxul.larix.service.TransactionService
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.stereotype.Component
import org.web3j.crypto.Sign
import java.math.BigDecimal
import java.time.LocalDateTime

@Component
class RedisMessageTransactionSubscriber(
    private val gson: Gson
) : MessageListener {

    val logger: Logger = LogManager.getLogger(RedisMessageTransactionSubscriber::class.java)

    override fun onMessage(message: Message, pattern: ByteArray?) {
        val msg: LinkedHashMap<*, *> = gson.fromJson(message.toString(), LinkedHashMap::class.java)
        val incomingTx = convertMessageToTx(msg)
        TransactionPool.add(incomingTx)
        logger.info("incoming tx '${incomingTx.txId}' is added the tx pool")
    }

    private fun convertMessageToTx(msg: LinkedHashMap<*, *>): Transaction {
        // get id
        val txId = msg["txId"] as String

        //crete empty tx
        val tx: Transaction = Transaction.getEmptyTxWithId(txId)

        // form outputMap for the tx
        (msg["outputMap"] as LinkedTreeMap<*, *>)
            .forEach { tx.outputMap[it.key as String] = BigDecimal(it.value as Double) }

        // form inputMap
        val im = msg["inputMap"] as LinkedTreeMap<*, *>

        // form timestamp
        val dateMap = (im["timestamp"] as LinkedTreeMap<*, *>)["date"] as LinkedTreeMap<*, *>
        val timeMap = (im["timestamp"] as LinkedTreeMap<*, *>)["time"] as LinkedTreeMap<*, *>
        tx.inputMap["timestamp"] = LocalDateTime.of(
            (dateMap["year"] as Double).toInt(),
            (dateMap["month"] as Double).toInt(),
            (dateMap["day"] as Double).toInt(),
            (timeMap["hour"] as Double).toInt(),
            (timeMap["minute"] as Double).toInt(),
            (timeMap["second"] as Double).toInt(),
        )

        // get amount
        tx.inputMap["amount"] = BigDecimal(im["amount"] as Double)
        // get address
        tx.inputMap["address"] = im["address"] as String

        // form signature
        val signatureMap = im["signature"] as LinkedTreeMap<*, *>

        val v: Byte = (signatureMap["v"] as Double).toInt().toByte()
        val r: ByteArray = (signatureMap["r"] as ArrayList<*>)
            .map { (it as Double).toInt().toByte() }
            .toByteArray()
        val s: ByteArray = (signatureMap["s"] as ArrayList<*>)
            .map { (it as Double).toInt().toByte() }
            .toByteArray()

        val signatureData = Sign.SignatureData(v, r, s)
        tx.inputMap["signature"] = signatureData

        return tx
    }
}