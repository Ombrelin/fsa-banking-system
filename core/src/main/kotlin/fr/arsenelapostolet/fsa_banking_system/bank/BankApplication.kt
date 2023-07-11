package fr.arsenelapostolet.fsa_banking_system.bank

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Account
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation
import fr.arsenelapostolet.fsa_banking_system.bank.persistance.AccountRepository
import java.math.BigDecimal

class BankApplication(private val accountRepository: AccountRepository) {

    suspend fun createAccount(accountHolderName: String): Account {
        val account = Account(accountHolderName)
        accountRepository.insert(account)

        return account
    }

    suspend fun listAccounts(): Collection<Account> {
        return accountRepository.getAll()
    }

    suspend fun paySalaries() {
        throw NotImplementedError()
    }

    suspend fun addDebit(operationName: String, amountBigDecimal: BigDecimal): Operation {
        throw NotImplementedError()
    }
}