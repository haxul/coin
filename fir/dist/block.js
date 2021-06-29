"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
class Block {
    constructor({ timestamp, lastHash, hash, data }) {
        this.toString = () => `
    Block:
       timestamp: ${this.timestamp},
       lastHash: ${this.lastHash},
       hash: ${this.hash},
       data: ${this.data}
    `;
        this.timestamp = timestamp;
        this.lastHash = lastHash;
        this.hash = hash;
        this.data = data;
    }
}
const b = new Block({ timestamp: 12, data: [], hash: "", lastHash: "" });
console.log(b.toString());
exports.default = Block;
//# sourceMappingURL=block.js.map