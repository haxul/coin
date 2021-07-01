package com.haxul.larix

import com.haxul.larix.property.AppProps
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AppProps::class)
class LarixApplication

fun main(args: Array<String>) {
    runApplication<LarixApplication>(*args)
}
