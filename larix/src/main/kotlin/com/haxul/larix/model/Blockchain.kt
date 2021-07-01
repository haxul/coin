package com.haxul.larix.model

import java.util.concurrent.CopyOnWriteArrayList

class Blockchain {
    var chain: MutableList<Block> = CopyOnWriteArrayList()

    init {
        chain.add(Block.GENESIS_BLOCK)
    }

    companion object {
        val STORAGE = Blockchain()
    }

    override fun toString(): String = "Blockchain:$chain"
}