package com.haxul.larix.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.haxul.larix.model.Block
import com.haxul.larix.property.AppProps
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.concurrent.CopyOnWriteArrayList


@Service
class SyncService(
    private val appProps: AppProps,
    private val gson: Gson
) : ApplicationListener<ContextRefreshedEvent> {

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val peers: List<String> = appProps.peers
        val peersBlockchains = getPeersBlockchains()
//        TODO("Not yet implemented")
    }

    private fun getPeersBlockchains(): String? {
        val restTemplate = RestTemplate()
        val json = restTemplate.getForEntity("http://localhost:8081/api/blocks", String::class.java)
        val listType = object : TypeToken<CopyOnWriteArrayList<Block>>() {}.type
        val outputList: List<Block> = gson.fromJson(json.body, listType)
        return json.body

    }
}