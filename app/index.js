const express = require("express")
const bodyParser = require("body-parser")
const Blockchain = require("../blockchain")
const HTTP_PORT = process.env.HTTP_PORT || 3001
const app = express()
const blockchain = new Blockchain()

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

app.listen(3001, () => console.log(`server starts on port ${HTTP_PORT}`))

