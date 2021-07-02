package com.haxul.larix.config

import com.google.gson.*
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.lang.reflect.Type
import java.time.Duration
import java.time.LocalDateTime

@Configuration
class AppConfig {

    @Bean
    fun gson(): Gson =
        GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, object : JsonDeserializer<LocalDateTime> {
                override fun deserialize(
                    json: JsonElement,
                    type: Type?,
                    jsonDeserializationContext: JsonDeserializationContext?
                ): LocalDateTime = LocalDateTime.parse(json.asString)
            }).create()


    @Bean
    fun restTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate =
        restTemplateBuilder
            .setConnectTimeout(Duration.ofSeconds(20L))
            .setReadTimeout(Duration.ofSeconds(40L))
            .build()

}