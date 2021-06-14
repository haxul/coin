const Block = require("./block")

class Blockchain {

    constructor() {
        this.chain = [Block.genesis()]
    }

    addBlock(data) {
        const lastBlock = this.chain[this.chain.length - 1]
        const block = Block.mineBlock(lastBlock, data)
        this.chain.push(block)
        return block
    }

    isValidChain(chain) {
        const inputFirstBlock = JSON.stringify(chain[0])
        const originGenesisBlock = JSON.stringify(Block.genesis())
        if (inputFirstBlock !== originGenesisBlock) return false
        for (let i = 1; i < chain.length; i++) {
            const block = chain[i]
            const lastBlock = chain[i - 1]
            const isValidLastHash = block.lastHash === lastBlock.hash
            const isValidCurrentHash = block.hash === Block.buildHashFromBlock(block)
            if (!isValidLastHash || !isValidCurrentHash) return false
        }
        return true
    }
}

module.exports = Blockchain