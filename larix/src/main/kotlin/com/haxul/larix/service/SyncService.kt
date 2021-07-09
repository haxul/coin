package com.haxul.larix.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.haxul.larix.config.GsonSyncDeserializer
import com.haxul.larix.model.Block
import com.haxul.larix.model.Blockchain
import com.haxul.larix.model.Transaction
import com.haxul.larix.model.TransactionPool
import com.haxul.larix.property.AppProps
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.web3j.crypto.Sign
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors
import kotlin.collections.LinkedHashMap

@Service
@Profile("!test")
class SyncService(
    private val appProps: AppProps,
    private val restTemplate: RestTemplate,
    private val gson: Gson
) : ApplicationListener<ContextRefreshedEvent> {

    private val logger: Logger = LogManager.getLogger(SyncService::class.java)

    override fun onApplicationEvent(event: ContextRefreshedEvent) {

        val executor = Executors.newFixedThreadPool(2)

        CompletableFuture.runAsync({
            Blockchain.STORAGE.chain = fetchRootPeerBlocks()
            logger.info("blockchain syncs root peer successfully")
        }, executor).exceptionally {
            logger.error("Cannot sync root peer: ${it.message}")
            null
        }

        CompletableFuture.runAsync({
            TransactionPool.txMap = fetchRootPeerTxPool()
            logger.info("tx pool was updated successfully")
        }, executor).exceptionally {
            logger.error("Cannot update tx pool: ${it.message}")
            null
        }
    }

    private fun fetchRootPeerTxPool(): MutableMap<String, Transaction> {
        val resp: ResponseEntity<String> = restTemplate
            .getForEntity("${appProps.rootPeer}/api/tx-pool", String::class.java)

        val body = gson.fromJson(resp.body, LinkedHashMap::class.java)
        return body.values.associate {
            val tx: Transaction = convertToTx(it as LinkedTreeMap<Any, Any>)
            tx.txId to tx
        }.toMutableMap()
    }

    private fun convertToTx(jsonTx: LinkedTreeMap<Any, Any>): Transaction {
        val tx: Transaction = Transaction.getEmptyTxWithId(jsonTx["txId"] as String)
        (jsonTx["outputMap"] as LinkedTreeMap<String, Double>)
            .forEach { tx.outputMap[it.key] = BigDecimal(it.value) }

        val jsonTxInputMap = jsonTx["inputMap"] as LinkedTreeMap<String, Any>
        tx.inputMap["timestamp"] = LocalDateTime.parse((jsonTxInputMap)["timestamp"].toString())
        tx.inputMap["amount"] = BigDecimal((jsonTxInputMap)["amount"] as Double)
        tx.inputMap["address"] = jsonTxInputMap["address"].toString()

        val jsonTxSignature = jsonTxInputMap["signature"] as LinkedTreeMap<String, Any>
        val v: Byte = (jsonTxSignature["v"] as Double).toInt().toByte()
        val r: ByteArray = Base64.getDecoder().decode(jsonTxSignature["r"] as String)
        val s: ByteArray = Base64.getDecoder().decode(jsonTxSignature["s"] as String)
        tx.inputMap["signature"] = Sign.SignatureData(v, r, s)

        return tx
    }

    private fun fetchRootPeerBlocks(): MutableList<Block> {
        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, GsonSyncDeserializer())
            .create()
        val json = restTemplate.getForEntity("${appProps.rootPeer}/api/blocks", String::class.java)
        val listType = object : TypeToken<CopyOnWriteArrayList<Block>>() {}.type
        return gson.fromJson(json.body, listType)
    }
}