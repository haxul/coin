import {GENESIS_DATA} from "./config";

export interface BlockAble {
    timestamp: number
    lastHash: string
    hash: string
    data: any
}

class Block {

    timestamp: number
    lastHash: string
    hash: string
    data: any

    constructor({timestamp, lastHash, hash, data}: BlockAble) {
        this.timestamp = timestamp
        this.lastHash = lastHash
        this.hash = hash
        this.data = data
    }

    public toString = (): string => `
    Block:
       timestamp: ${this.timestamp},
       lastHash: ${this.lastHash},
       hash: ${this.hash},
       data: ${this.data}
    `
    public static getGenesis = (): Block => new Block({
        timestamp: GENESIS_DATA.timestamp,
        data: GENESIS_DATA.data,
        hash: GENESIS_DATA.hash,
        lastHash: GENESIS_DATA.lastHash
    });

}

export default Block