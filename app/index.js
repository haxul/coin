const express = require("express")
const bodyParser = require("body-parser")
const HTTP_PORT = process.env.HTTP_PORT || 3001
const app = express()

const Blockchain = require("../blockchain")
const blockchain = new Blockchain()

const P2PServer = require("./p2p-server")
const p2pServer = new P2PServer(blockchain)

app.use(bodyParser.json())

app.get("/blocks", (req, res) => {
    res.json(blockchain.chain)
})

app.post("/mine", (req, res) => {
    const data = req.body.data;
    const block = blockchain.addBlock(data)
    console.log(`new block added: ${block.toString()}`)
    res.redirect("/blocks")
})

app.listen(HTTP_PORT, () => console.log(`server starts on port ${HTTP_PORT}`))
p2pServer.listen()
