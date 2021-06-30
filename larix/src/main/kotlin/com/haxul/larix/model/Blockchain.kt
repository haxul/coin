package com.haxul.larix.model

import java.util.concurrent.CopyOnWriteArrayList

class Blockchain {
    var chain: CopyOnWriteArrayList<Block> = CopyOnWriteArrayList()

    init {
        chain.add(Block.GENESIS_BLOCK)
    }

    override fun toString(): String = "Blockchain:$chain"
}