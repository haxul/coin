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

    it("not allowed to update when it is not enough money in the wallet", () => {
        const nextAmount = 200000000
        const nextRecipient = "!!!!!!"
        transaction.update(wallet, nextRecipient, nextAmount)
        expect(transaction.outputs.find(out => out.address === wallet.publicKey).amount)
            .toEqual(wallet.balance - amount)
    })

    it("update a transaction", () => {
        const nextAmount = 20
        const nextRecipient = "!!!!!!"
        transaction.update(wallet, nextRecipient, nextAmount)
        expect(transaction.outputs.find(out => out.address === wallet.publicKey).amount)
            .toEqual(wallet.balance - amount - nextAmount)
    })

    it("update a transaction", () => {
        const nextAmount = 20
        const nextRecipient = "!!!!!!"
        transaction.update(wallet, nextRecipient, nextAmount)
        expect(transaction.outputs.find(out => out.address === nextRecipient).amount)
            .toEqual(nextAmount)
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

    it("inputs the balance of the wallet", () => {
        expect(transaction.input.amount).toEqual(wallet.balance)
    })

    it("validates a valid transaction", () => {
        expect(Transaction.verifyTransaction(transaction)).toBe(true)
    })

    it("invalidates a not valid transaction", () => {
        transaction.outputs[0].amount = 5000000
        expect(Transaction.verifyTransaction(transaction)).toBe(false)
    })
})