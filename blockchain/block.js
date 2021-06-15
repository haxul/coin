const SHA256 = require("crypto-js/sha256")
const {DIFFICULTY} = require("../config")

class Block {

    constructor(timestamp, lastHash, hash, data, nonce) {
        this.timestamp = timestamp
        this.lastHash = lastHash
        this.hash = hash
        this.data = data
        this.nonce = nonce
    }

    toString() {
        return `Block -
        Timestamp: ${this.timestamp},
        Last Hash: ${this.lastHash.substring(0, 10)},
        Hash     : ${this.hash.substring(0, 10)},
        Nonce    : ${this.nonce},
        Data     : ${this.data}`
    }

    static genesis() {
        return new Block("Genesis Time", "----", "f1r57-h45h", [], 0)
    }

    static mineBlock(lastBlock, data) {
        const lastHash = lastBlock.hash
        let hash, timestamp
        let nonce = 0
        do {
            nonce++
            hash = Block.buildHash(timestamp, lastHash, data, nonce)
        } while (hash.substring(0, DIFFICULTY) !== "0".repeat(DIFFICULTY))

        return new Block(timestamp, lastHash, hash, data, nonce)
    }

    static buildHash(timestamp, lastHash, data, nonce) {
        return SHA256(`${timestamp}${lastHash}${data}${nonce}`).toString()
    }

    static buildHashFromBlock(block) {
        const {timestamp, lastHash, data, nonce} = block
        return Block.buildHash(timestamp, lastHash, data, nonce)
    }
}


module.exports = Block