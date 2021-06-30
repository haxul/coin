package com.haxul.larix.model

class Blockchain {
    var chain: MutableList<Block> = mutableListOf(Block.GENESIS_BLOCK)
    override fun toString(): String = "Blockchain:$chain"
}