const Transaction = require("../wallet/transaction")
const Wallet = require("../wallet")


describe("Transaction", () => {
    let transaction, wallet, recipient, amount

    beforeEach(() => {
        wallet = new Wallet()
        amount = 50
        recipient = "recipientFree"
        transaction = Transaction.newTransaction(wallet, recipient, amount)
    })

    it("outputs the `amount` subtracted from the wallet balance", () => {
        expect(transaction.outputs.find(out => out.address === wallet.publicKey).amount)
            .toEqual(wallet.balance - amount)
    })

    it("outputs the `amount` added to recipient", () => {
        expect(transaction.outputs.find(out => out.address === recipient).amount)
            .toEqual(amount)
    })

    it("amount is greater than wallet balance", () => {
        const amount = 1000
        const transaction = Transaction.newTransaction(wallet, recipient, amount)
        expect(transaction).toEqual(undefined)
    })
})