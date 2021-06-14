const Blockchain = require("../blockchain")
const Block = require("../block")

describe("Blockchain", () => {

    let bc, bc2

    beforeEach(() => {
        bc = new Blockchain()
        bc2 = new Blockchain()
    })

    it ("does not replace the chain with length equal or less",() => {
        bc.addBlock("foo")
        bc.replaceChain(bc2.chain)
        expect(bc.chain).not.toEqual(bc2.chain)
    })

    it("replaces chain with valid chain", () => {
        bc2.addBlock("google")
        bc.replaceChain(bc2.chain)

        expect(bc.chain).toEqual(bc2.chain)
    })

    it("validates a valid chain", () => {
        bc2.addBlock("foo")
        expect(bc.isValidChain(bc2.chain)).toBe(true)
    })


    it("when create Blockchain then Blockchain has genesis block", () => {
        expect(bc.chain[0]).toEqual(Block.genesis())
    })

    it("add Block in Blockchain", () => {
        const data = "foo"
        bc.addBlock(data)

        expect(bc.chain[bc.chain.length - 1].data).toEqual(data)
    })

    it("invalidates a chain with a corrupt genesis block", () => {
        bc2.chain[0].data = "Bad data"
        expect(bc.isValidChain(bc2.chain)).toBe(false)
    })

    it("invalidates a corrupt chain data", () => {
        bc2.addBlock("foo")
        bc2.chain[1].data = "Not foo"
        expect(bc.isValidChain(bc2.chain)).toBe(false)
    })

    it("invalidates a corrupt chain timestamp", () => {
        bc2.addBlock("foo")
        bc2.chain[1].timestamp = Date.now() - 1000
        expect(bc.isValidChain(bc2.chain)).toBe(false)
    })

    it("invalidates a corrupt chain lastHash", () => {
        bc2.addBlock("foo")
        bc2.chain[1].lastHash = "hoho"
        expect(bc.isValidChain(bc2.chain)).toBe(false)
    })
})