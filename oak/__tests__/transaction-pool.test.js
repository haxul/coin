const TransactionPool = require("../wallet/transaction-pool")
const Wallet = require("../wallet")
const Transaction = require("../wallet/transaction")
const Blockchain = require("../blockchain")

describe("Transaction", () => {
    let tp, wallet, transaction, bc

    beforeEach(() => {
        tp = new TransactionPool()
        wallet = new Wallet()
        bc = new Blockchain()
        transaction = wallet.createTransaction("111", 30, bc, tp)
    })

    it("adds a transaction to the pool", () => {
        expect(tp.transactions.find(t => t.id === transaction.id)).toEqual(transaction)
    })

    it("updates a transaction in the pool", () => {
        const oldTransaction = JSON.stringify(transaction)
        const newTransaction = transaction.update(wallet, "22222!!", 40)
        tp.updateOrAddTransaction(newTransaction)
        expect(JSON.stringify(tp.transactions.find(t => t.id === newTransaction.id)))
            .not.toEqual(oldTransaction)
    })

    it("clears transactions", () => {
        tp.clear()
        expect(tp.transactions).toEqual([])
    })

    describe("mixing valid and corrupt transaction", () => {
        let validTransactions

        beforeEach(() => {
            validTransactions = [...tp.transactions]
            for (let i = 0; i < 6; i++) {
                wallet = new Wallet()
                transaction = wallet.createTransaction("foo-123", 30, bc, tp)
                if (i % 2 === 0) transaction.input.amount = 9999999
                else validTransactions.push(transaction)
            }
        })

        it("shows a difference between valid and corrupt transactions", () => {
            expect(JSON.stringify(tp.transactions)).not.toEqual(JSON.stringify(validTransactions))
        })

        it("grabs valid transactions", () => {
            expect(tp.validTransactions()).toEqual(validTransactions)
        })
    })
})