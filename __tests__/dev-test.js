const assert = require("assert")
const Block = require("../block")

const block = new Block("foo", "bar", "zoo", "baz")

// check toString of Block
assert.strictEqual(
    'Block -\n' +
    '        Timestamp: foo,\n' +
    '        Last Hash: bar,\n' +
    '        Hash     : zoo,\n' +
    '        Data     : baz'
    , block.toString())

// check block mining
const fooBlock = Block.mineBlock(Block.genesis(), ["hello"])
assert.strictEqual(fooBlock.data[0], "hello")
assert.strictEqual(fooBlock.hash !== undefined, true)
assert.strictEqual(fooBlock.lastHash, "f1r57-h45h")
assert.strictEqual(Number.isInteger(fooBlock.timestamp), true)

console.log("tests passed")