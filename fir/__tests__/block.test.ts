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
        const genesisBlock: Block = Block.getGenesis()
        console.log("genesis block: ", genesisBlock)
        it("returns genesis block", () => {
            expect(genesisBlock.data).toEqual(GENESIS_DATA.data)
            expect(genesisBlock.timestamp).toEqual(GENESIS_DATA.timestamp)
            expect(genesisBlock.lastHash).toEqual(GENESIS_DATA.lastHash)
            expect(genesisBlock.hash).toEqual(GENESIS_DATA.hash)
        })
    })

    describe("mineblock", () => {
        const lastBlock: Block = Block.getGenesis();
        const data = "mined data"
        const minedBlock: Block = Block.mineBlock({lastBlock, data})
        console.log("mined block:", minedBlock)
        it("sets the `lastHash` to be the `hash` of the last block", () => {
            expect(minedBlock.lastHash).toEqual(lastBlock.hash)
        })

        it("sets the `data`", () => {
            expect(minedBlock.data).toEqual(data)
        })

        it("sets a `timestamp`", () => {
            expect(minedBlock.timestamp).not.toEqual(undefined)
        })
    })
})