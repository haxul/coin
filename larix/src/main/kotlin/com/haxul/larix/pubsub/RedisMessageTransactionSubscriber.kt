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
    private val gson: Gson,
) : MessageListener {

    val logger: Logger = LogManager.getLogger(RedisMessageTransactionSubscriber::class.java)

    override fun onMessage(message: Message, pattern: ByteArray?) {
        val msg: LinkedHashMap<*, *> = gson.fromJson(message.toString(), LinkedHashMap::class.java)
        val incomingTx = convertMessageToTx(msg)
        TransactionPool.add(incomingTx)
        logger.info("incoming tx '${incomingTx.txId}' is added the tx pool")
    }


    // TODO write tests and refactor
    fun convertMessageToTx(msg: LinkedHashMap<*, *>): Transaction {

        val txId = msg["txId"] as String
        val tx: Transaction = Transaction.getEmptyTxWithId(txId)
        tx.outputMap.clear()
        tx.inputMap.clear()

        val rawOutputMap = msg["outputMap"] as LinkedTreeMap<*, *>
        rawOutputMap.forEach {
            tx.outputMap[it.key as String] = BigDecimal(it.value as Double)
        }

        val rim = msg["inputMap"] as LinkedTreeMap<*, *>
        val dateMap = (rim["timestamp"] as LinkedTreeMap<*, *>)["date"] as LinkedTreeMap<*, *>
        val timeMap = (rim["timestamp"] as LinkedTreeMap<*, *>)["time"] as LinkedTreeMap<*, *>

        tx.inputMap["timestamp"] = LocalDateTime.of(
            (dateMap["year"] as Double).toInt(),
            (dateMap["month"] as Double).toInt(),
            (dateMap["day"] as Double).toInt(),
            (timeMap["hour"] as Double).toInt(),
            (timeMap["minute"] as Double).toInt(),
            (timeMap["second"] as Double).toInt(),
        )

        tx.inputMap["amount"] = BigDecimal(rim["amount"] as Double)
        tx.inputMap["address"] = rim["address"] as String


        val signatureMap = rim["signature"] as LinkedTreeMap<*, *>
        val v: Byte = (signatureMap.get("v") as Double).toInt().toByte()
        val rrList: List<Byte> = (signatureMap.get("r") as ArrayList<*>).map { (it as Double).toInt().toByte() }
        val r = ByteArray(rrList.size)
        for ((i, e) in rrList.withIndex()) {
            r[i] = e
        }
        val rsList: List<Byte> = (signatureMap.get("s") as ArrayList<*>).map { (it as Double).toInt().toByte() }
        val s = ByteArray(rsList.size)
        for ((i, e) in rsList.withIndex()) {
            s[i] = e
        }
        val signatureData = Sign.SignatureData(
            v, r, s
        )
        tx.inputMap["signature"] = signatureData

        return tx
    }
}