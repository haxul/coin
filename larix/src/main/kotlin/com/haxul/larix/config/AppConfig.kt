package com.haxul.larix.config

import com.google.gson.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.lang.reflect.Type
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
}