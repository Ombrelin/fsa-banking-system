package fr.arsenelapostolet.fsa_banking_system.app.database.repositories

import fr.arsenelapostolet.fsa_banking_system.app.database.entities.DatabaseOperation
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation
import fr.arsenelapostolet.fsa_banking_system.bank.persistance.OperationRepository
import org.ufoss.kotysa.PostgresqlR2dbcSqlClient

class PostgresOperationRepository(private val client: PostgresqlR2dbcSqlClient) : OperationRepository {
    override suspend fun insert(operation: Operation, accountName: String) {
        client.insert(DatabaseOperation(operation, accountName))
    }
}