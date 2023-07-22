package fr.arsenelapostolet.fsa_banking_system.bank

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Account
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Rank
import fr.arsenelapostolet.fsa_banking_system.bank.persistance.AccountRepository
import fr.arsenelapostolet.fsa_banking_system.bank.persistance.OperationRepository
import fr.arsenelapostolet.fsa_banking_system.bank.persistance.RankRepository
import java.math.BigDecimal

class BankApplication (
    private val accountRepository: AccountRepository,
    private val operationRepository: OperationRepository,
    private val rankRepository: RankRepository
) {

    suspend fun createAccount(accountHolderName: String, rankName: String): Account {
        val account = Account(accountHolderName, rankName)
        accountRepository.insert(account)

        return account
    }

    suspend fun listAccounts(): Collection<Account> {
        return accountRepository.getAll()
    }

    suspend fun createRank(rankName: String, rankSalary: BigDecimal): Rank{
        val rank = Rank(rankName, rankSalary)
        rankRepository.insert(rank)

        return rank
    }

    suspend fun listRanks(): Collection<Rank> = rankRepository.getAll()

    suspend fun accountDetails(accountName: String): Account? {
        return accountRepository.getByName(accountName)
    }

    suspend fun paySalaries() {
        val ranks = rankRepository
            .getAll()
            .associate { it.name to it.salary }

        val accounts = accountRepository
            .getAll()

        for (account in accounts){
            operationRepository.insert(Operation(Operation.OperationKind.CREDIT, ranks[account.rankName]!!), account.name)
        }
    }

    suspend fun promoteAccount(accountName: String, rankName: String){
        val account = accountRepository.getByName(accountName)
        account!!.rankName = rankName
        accountRepository.updateAccount(account)
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