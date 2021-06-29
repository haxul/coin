import Block from "../block";
import {GENESIS_DATA} from "../config";

describe("Block", () => {
    const timestamp = 123
    const lastHash = "last-hash"
    const hash = "hash"
    const data = ["blockchain", "data"]
    const block = new Block({
        timestamp,
        lastHash,
        hash,
        data
    })

    it("has correct fields", () => {
        expect(block.timestamp).toEqual(timestamp)
        expect(block.lastHash).toEqual(lastHash)
        expect(block.hash).toEqual(hash)
        expect(block.data).toEqual(data)
    })

    describe("genesis block", () => {
        const genesisBlock:Block = Block.getGenesis()
        it("returns genesis block", () => {
            expect(genesisBlock.data).toEqual(GENESIS_DATA.data)
            expect(genesisBlock.timestamp).toEqual(GENESIS_DATA.timestamp)
            expect(genesisBlock.lastHash).toEqual(GENESIS_DATA.lastHash)
            expect(genesisBlock.hash).toEqual(GENESIS_DATA.hash)
        })
    })
})