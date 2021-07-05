package com.haxul.larix.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.haxul.larix.config.GsonSyncDeserializer
import com.haxul.larix.model.Block
import com.haxul.larix.model.Blockchain
import com.haxul.larix.property.AppProps
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors

@Service
class SyncService(
    private val appProps: AppProps,
    private val env: Environment,
    private val restTemplate: RestTemplate
) : ApplicationListener<ContextRefreshedEvent> {


    val logger: Logger = LogManager.getLogger(SyncService::class.java)
    val isTestProfile: Boolean = env.activeProfiles.contains("test")

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        if (isTestProfile) return
        val executor = Executors.newSingleThreadExecutor()
        CompletableFuture.runAsync({
            Blockchain.STORAGE.chain = fetchRootPeerBlocks()
            logger.info("blockchain syncs root peer successfully")
        }, executor).exceptionally {
            logger.error("Cannot sync root peer: ${it.message}")
            null
        }
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