const express = require("express")
const bodyParser = require("body-parser")
const Wallet = require("../wallet")
const TransactionPool = require("../wallet/transaction-pool")
const Blockchain = require("../blockchain")
const P2PServer = require("./p2p-server")
const Miner = require("./miner")

const HTTP_PORT = process.env.HTTP_PORT || 3001
const app = express()

const blockchain = new Blockchain()
const wallet = new Wallet()
const transactionPool = new TransactionPool()
const p2pServer = new P2PServer(blockchain, transactionPool)
const miner = new Miner(blockchain, transactionPool, wallet, p2pServer)

app.use(bodyParser.json())

app.get("/blocks", (req, res) => {
    res.json(blockchain.chain)
})

app.post("/mine", (req, res) => {
    const data = req.body.data;
    const block = blockchain.addBlock(data)
    console.log(`new block added: ${block.toString()}`)

    p2pServer.syncChains()
    res.redirect("/blocks")
})

app.get("/transactions", (req, res) => {
    res.json(transactionPool.transactions)
})

app.post("/transact", (req, res) => {
    const {recipient, amount} = req.body
    const transaction = wallet.createTransaction(recipient, blockchain, amount, transactionPool)
    p2pServer.broadCoastTransaction(transaction)
    res.redirect("/transactions")
})

app.get("/public-key", (req, res) => {
    res.json({publicKey: wallet.publicKey})
})

app.get("/mine-transactions", (req, res) => {
    const block = miner.mine()
    console.log(`New block added: ${block.toString()}`)
    res.redirect("/blocks")
})

app.listen(HTTP_PORT, () => console.log(`server starts on port ${HTTP_PORT}`))
p2pServer.listen()
