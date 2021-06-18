const ChainUtil = require("../chain-util")
const {INITIAL_BALANCE} = require("../config")
const Transaction = require("./transaction")

class Wallet {

    constructor() {
        this.balance = INITIAL_BALANCE;
        this.keyPair = ChainUtil.genKeyPair()
        this.publicKey = this.keyPair.getPublic().encode("hex")
    }

    toString() {
        return `Wallet - 
        publicKey: ${this.publicKey.toString()}
        balance  : ${this.balance}`
    }

    sign(dataHash) {
        return this.keyPair.sign(dataHash)
    }

    createTransaction(recipient, amount, blockchain, transactionPool) {
        this.balance = this.calculateBalance(blockchain)
        if (amount > this.balance) {
            console.log(`Amount: ${amount} exceeds current balance: ${this.balance}`)
            return
        }

        let transaction = transactionPool.existingTransaction(this.publicKey)
        if (transaction) transaction.update(this, recipient, amount)
        else {
            transaction = Transaction.newTransaction(this, recipient, amount)
            transactionPool.updateOrAddTransaction(transaction)
        }
        return transaction
    }

    calculateBalance(blockchain) {
        let balance = this.balance
        const transactions = []
        blockchain.chain.forEach(block => {
            block.data.forEach(transaction => {
                transactions.push(transaction)
            })
        })

        let startTime = 0
        const walletInputs = transactions.filter(transaction => transaction.input.address === this.publicKey)

        if (walletInputs.length > 0) {
            const recentInput = walletInputs.reduce((prev, current) => prev.infoAccess.timestamp > current.input.timestamp ? prev : current)
            balance = recentInput.outputs.find(out => out.address === this.publicKey).amount
            startTime = recentInput.input.timestamp
        }

        transactions.forEach(transaction => {
            if (transaction.input.timestamp > startTime) {
                transaction.outputs.find(out => {
                    if (out.address === this.publicKey) {
                        balance += out.amount
                    }
                })
            }
        })

        return balance
    }

    static blockchainWallet() {
        const blockchainWallet = new this()
        blockchainWallet.publicKey = "blockchain-wallet"
        return blockchainWallet
    }
}

module.exports = Wallet