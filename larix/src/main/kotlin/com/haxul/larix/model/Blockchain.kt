package com.haxul.larix.model

class Blockchain {
    val chain = mutableListOf(Block.GENESIS_BLOCK)
    override fun toString(): String = "Blockchain:$chain"
}