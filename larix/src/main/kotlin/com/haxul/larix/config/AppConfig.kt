package com.haxul.larix.config

import com.google.gson.Gson
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class AppConfig {

    @Bean
    fun gson(): Gson = Gson()

    @Bean
    fun restTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate =
        restTemplateBuilder
            .setConnectTimeout(Duration.ofSeconds(20L))
            .setReadTimeout(Duration.ofSeconds(40L))
            .build()

}