package com.haxul.larix.property

import com.haxul.larix.exception.PropsInitException
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "app")
@ConstructorBinding
data class AppProps(
    val peers: List<String>
) {
    init {
        if (peers.isEmpty()) throw PropsInitException("peers of AppProps is empty")
    }
}