export interface BlockAble {
    timestamp: number
    lastHash: string
    hash: string
    data: any
}

export class Block {

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
}
