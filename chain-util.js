const EC = require("elliptic").ec
const ec = new EC("secp256k1")
const { v4: uuidv4 } = require('uuid');

class ChainUtil {

    static genKeyPair() {
        return ec.genKeyPair()
    }

    static id() {
        return uuidv4()
    }
}

module.exports = ChainUtil