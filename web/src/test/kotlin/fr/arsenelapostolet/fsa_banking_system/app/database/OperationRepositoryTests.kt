package fr.arsenelapostolet.fsa_banking_system.app.database

import fr.arsenelapostolet.fsa_banking_system.app.database.entities.DatabaseAccount
import fr.arsenelapostolet.fsa_banking_system.app.database.repositories.PostgresOperationRepository
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.ufoss.kotysa.PostgresqlR2dbcSqlClient
import java.math.BigDecimal
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OperationRepositoryTests {

    private var client: PostgresqlR2dbcSqlClient? = null
    private var target: PostgresOperationRepository? = null
    private val database = DatabaseFixture()

    @BeforeAll
    fun setup() {
        client = database.client
        target = PostgresOperationRepository(client!!)
        runBlocking {
            database.migrate()
            database.client.deleteAllFrom(DatabaseOperations)
            database.client.deleteAllFrom(DatabaseAccounts)
        }
    }

    @Test
    fun insert_insertsRecordInDb() = runBlocking {
        // Given
        val name = "Test Account Name"
        client!!.insert(DatabaseAccount(name))

        val operation = Operation(Operation.OperationKind.DEBIT, BigDecimal(18))

        // When
        target!!.insert(operation, name)

        // Then
        var recordCount = (client!!
                selectCountFrom DatabaseOperations
                where DatabaseOperations.accountname eq name
                and DatabaseOperations.kind eq Operation.OperationKind.DEBIT.toString()
                and DatabaseOperations.amount eq BigDecimal(18)
                )
            .fetchOne()
        assertEquals(1, recordCount)
    }

    @AfterAll
    fun cleanUp() {
        runBlocking {
            database.client.deleteAllFrom(DatabaseOperations)
            database.client.deleteAllFrom(DatabaseAccounts)
        }
    }
}