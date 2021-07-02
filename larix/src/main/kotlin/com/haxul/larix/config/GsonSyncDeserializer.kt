package com.haxul.larix.config

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDateTime

class GsonSyncDeserializer : JsonDeserializer<LocalDateTime> {

    override fun deserialize(json: JsonElement, p1: Type?, p2: JsonDeserializationContext?): LocalDateTime {
        return LocalDateTime.parse(json.asString)
    }
}