import {Block} from "../block";

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
})