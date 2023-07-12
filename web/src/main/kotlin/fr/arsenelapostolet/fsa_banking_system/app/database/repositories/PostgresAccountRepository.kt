package fr.arsenelapostolet.fsa_banking_system.app.database.repositories

import fr.arsenelapostolet.fsa_banking_system.app.database.DatabaseAccounts
import fr.arsenelapostolet.fsa_banking_system.app.database.DatabaseOperations
import fr.arsenelapostolet.fsa_banking_system.app.database.entities.DatabaseAccount
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Account
import fr.arsenelapostolet.fsa_banking_system.bank.persistance.AccountRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.ufoss.kotysa.PostgresqlR2dbcSqlClient

class PostgresAccountRepository(private val client: PostgresqlR2dbcSqlClient) : AccountRepository {

    override suspend fun insert(account: Account) {
        client.insert(DatabaseAccount(account))
    }

    override suspend fun getByName(accountName: String): Account? {
        val record = (client
                selectFrom DatabaseAccounts
                where DatabaseAccounts.name eq accountName)
            .fetchOneOrNull()

        val operations = (client
                selectFrom DatabaseOperations
                where DatabaseOperations.accountname eq accountName
                ).fetchAll()
            .toList()
            .map { it.toDomainEntity() }

        return record
            ?.toDomainEntity(operations)
    }

    override suspend fun getAll(): Collection<Account> {
        return (client selectFrom DatabaseAccounts)
            .fetchAll()
            .map { it.toDomainEntity() }
            .toList()
    }
}