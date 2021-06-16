const Wallet = require("../wallet")
const Transaction = require("../wallet/transaction")
const TransactionPool = require("../wallet/transaction-pool")


describe("Wallet", () => {
    let wallet, tp

    beforeEach(() => {
        wallet = new Wallet()
        tp = new TransactionPool()
    })

    describe("creating transaction", () => {

        let sendAmount, recipient, transaction

        beforeEach(() => {
            sendAmount = 50
            recipient = "helloworld"
            transaction = wallet.createTransaction(recipient, sendAmount, tp)
        })


        describe(" and doing the same transaction", () => {
            beforeEach(() => {
                wallet.createTransaction(recipient, sendAmount, tp)
            })

            it("doubles the `sendAmount` subtracted from the wallet balance", () => {
                expect(transaction.outputs.find(out => out.address === wallet.publicKey).amount)
                    .toEqual(wallet.balance - (sendAmount * 2))
            })

            it("clones the `sendAmount` output for the recipient", () => {
                expect(transaction.outputs
                    .filter(out => out.address === recipient)
                    .map(out => out.amount)).toEqual([sendAmount, sendAmount])

            })
        })
    })
})