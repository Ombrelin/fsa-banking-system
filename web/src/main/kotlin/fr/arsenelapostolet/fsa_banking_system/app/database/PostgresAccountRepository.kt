package fr.arsenelapostolet.fsa_banking_system.app.database

import fr.arsenelapostolet.fsa_banking_system.app.database.entities.DatabaseAccount
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Account
import fr.arsenelapostolet.fsa_banking_system.bank.persistance.AccountRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.flow.toList
import org.ufoss.kotysa.PostgresqlR2dbcSqlClient

class PostgresAccountRepository(private val client: PostgresqlR2dbcSqlClient) : AccountRepository {

    override suspend fun insert(account: Account) {
        client.insert(DatabaseAccount(account))
    }

    override suspend fun getByName(accountName: String): Account? {
        throw NotImplementedError()
    }

    override suspend fun getAll(): Collection<Account> {
        return (client selectFrom DatabaseAccounts)
            .fetchAll()
            .map { it.toDomainEntity() }
            .toList()
    }
}