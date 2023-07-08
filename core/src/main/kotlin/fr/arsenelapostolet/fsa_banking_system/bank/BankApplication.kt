package fr.arsenelapostolet.fsa_banking_system.bank

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Account
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation
import fr.arsenelapostolet.fsa_banking_system.bank.persistance.AccountRepository
import java.math.BigDecimal

class BankApplication(private val accountRepository: AccountRepository) {

    fun createAccount(accountHolderName: String): Account{
        throw NotImplementedError()
    }

    fun listAccounts(): Set<Account>{
        throw NotImplementedError()
    }

    fun paySalaries() {
        throw NotImplementedError()
    }

    fun addDebit(operationName: String, amountBigDecimal: BigDecimal): Operation {
        throw NotImplementedError()
    }
}