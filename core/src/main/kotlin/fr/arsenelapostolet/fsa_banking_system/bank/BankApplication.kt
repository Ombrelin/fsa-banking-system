package fr.arsenelapostolet.fsa_banking_system.bank

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Account
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation
import fr.arsenelapostolet.fsa_banking_system.bank.persistance.AccountRepository
import fr.arsenelapostolet.fsa_banking_system.bank.persistance.OperationRepository
import java.math.BigDecimal

class BankApplication constructor(
    private val accountRepository: AccountRepository,
    private val operationRepository: OperationRepository
) {

    suspend fun createAccount(accountHolderName: String): Account {
        val account = Account(accountHolderName)
        accountRepository.insert(account)

        return account
    }

    suspend fun listAccounts(): Collection<Account> {
        return accountRepository.getAll()
    }

    suspend fun accountDetails(accountName: String): Account? {
        return accountRepository.getByName(accountName)
    }

    suspend fun paySalaries() {
        throw NotImplementedError()
    }

    suspend fun addOperation(
        operationKind: Operation.OperationKind,
        amount: BigDecimal,
        accountName: String
    ): Operation {
        val operation = Operation(operationKind, amount)
        operationRepository.insert(operation, accountName)

        return operation;
    }
}