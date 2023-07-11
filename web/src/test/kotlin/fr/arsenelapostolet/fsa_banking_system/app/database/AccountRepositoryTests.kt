package fr.arsenelapostolet.fsa_banking_system.app.database

import fr.arsenelapostolet.fsa_banking_system.app.database.entities.DatabaseAccount
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Account
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.ufoss.kotysa.PostgresqlR2dbcSqlClient
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountRepositoryTests {

    private var client: PostgresqlR2dbcSqlClient? = null
    private var target: PostgresAccountRepository? = null
    private val database = DatabaseFixture()

    @BeforeAll
    fun setup() {
        client = database.client
        target = PostgresAccountRepository(client!!)
        runBlocking {
            database.migrate()
            database.client.deleteAllFrom(DatabaseAccounts)
        }
    }

    @Test
    fun insert_insertsRecordInDb() = runBlocking {
        // Given
        val name = "Test Account Name"

        // When
        target!!.insert(Account(name))

        // Then
        var recordCount = (client!! selectCountFrom DatabaseAccounts where DatabaseAccounts.name eq name)
            .fetchOne()
        assertEquals(1, recordCount)
    }

    @Test
    fun getAll_returnsAllRecordsInDb() = runBlocking {
        // Given
        val records = listOf(
            DatabaseAccount("Test account 1"),
            DatabaseAccount("Test account 2"),
            DatabaseAccount("Test account 3")
        )
        for (account in records) {
            client!!.insert(account)
        }
        // When
        val result = target!!.getAll()

        // Then
        assertEquals(3, result.size)
        val accountNames = result.map { it.name }
        for (account in result) {
            assertTrue(accountNames.contains(account.name))
        }
    }

    @AfterAll
    fun cleanUp() {
        runBlocking {
            database.client.deleteAllFrom(DatabaseAccounts)
        }
    }
}