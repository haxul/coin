const Block = require("./block")

class Index {

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

    replaceChain(newChain) {
        if (newChain.length <= this.chain.length) {
            console.log("received chain is not longer than current chain.")
            return
        }

        if (!this.isValidChain(newChain)) {
            console.log("received chain is not valid")
            return
        }

        console.log("Replacing blockchain with new chain.")
        this.chain = newChain
    }
}

module.exports = Index