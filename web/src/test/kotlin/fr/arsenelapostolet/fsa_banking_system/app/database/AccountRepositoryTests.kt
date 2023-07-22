package fr.arsenelapostolet.fsa_banking_system.app.database

import fr.arsenelapostolet.fsa_banking_system.app.database.entities.DatabaseAccount
import fr.arsenelapostolet.fsa_banking_system.app.database.entities.DatabaseOperation
import fr.arsenelapostolet.fsa_banking_system.app.database.entities.DatabaseRank
import fr.arsenelapostolet.fsa_banking_system.app.database.repositories.PostgresAccountRepository
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Account
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.ufoss.kotysa.PostgresqlR2dbcSqlClient
import java.math.BigDecimal
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountRepositoryTests {

    private var client: PostgresqlR2dbcSqlClient? = null
    private var target: PostgresAccountRepository? = null
    private val database = DatabaseFixture()

    @BeforeEach
    fun setup() {
        client = database.client
        target = PostgresAccountRepository(client!!)
        runBlocking {
            database.migrate()
            database.client.deleteAllFrom(DatabaseOperations)
            database.client.deleteAllFrom(DatabaseAccounts)
            database.client.deleteAllFrom(DatabaseRanks)
        }
    }

    @Test
    fun insert_insertsRecordInDb() = runBlocking {
        // Given
        val rankName = "test rank";
        client!!.insert(DatabaseRank(rankName, BigDecimal(3)))
        val name = "Test Account Name"

        // When
        target!!.insert(Account(name, rankName))

        // Then
        val recordCount = (client!! selectCountFrom DatabaseAccounts where DatabaseAccounts.name eq name)
            .fetchOne()
        assertEquals(1, recordCount)
    }

    @Test
    fun getAll_returnsAllRecordsInDb() = runBlocking {
        // Given
        val rankName = "test rank";
        client!!.insert(DatabaseRank(rankName, BigDecimal(3)))
        val records = listOf(
            DatabaseAccount("Test account 1", rankName),
            DatabaseAccount("Test account 2", rankName),
            DatabaseAccount("Test account 3", rankName)
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

    @Test
    fun getByName_existingAccount_returns() = runBlocking {
        // Given
        val rankName = "test rank";
        client!!.insert(DatabaseRank(rankName, BigDecimal(3)))
        val accountName = "Test Account"
        client!!.insert(DatabaseAccount(accountName, rankName))

        // When
        val result = target!!.getByName(accountName)

        // Then
        assertNotNull(result)
        assertEquals(accountName, result.name)
    }

    @Test
    fun getByName_existingWithOperations_returnsOperations() = runBlocking {
        // Given
        val rankName = "test rank";
        client!!.insert(DatabaseRank(rankName, BigDecimal(3)))
        val accountName = "Test Account"
        client!!.insert(DatabaseAccount(accountName, rankName))
        val operation1 =
            DatabaseOperation(UUID.randomUUID(), Operation.OperationKind.CREDIT.toString(), BigDecimal(18), accountName)
        val operation2 =
            DatabaseOperation(UUID.randomUUID(), Operation.OperationKind.DEBIT.toString(), BigDecimal(20), accountName)
        client!!.insert(operation1)
        client!!.insert(operation2)

        // When
        val result = target!!.getByName(accountName)

        // Then
        assertNotNull(result)
        assertEquals(accountName, result.name)
        assertEquals(2, result.operations.size)
        val operation1inResult = result.operations.find { it.id == operation1.id }
        val operation2inResult = result.operations.find { it.id == operation2.id }
        assertNotNull(operation1inResult)
        assertNotNull(operation2inResult)

        assertEquals(operation1.amount.intValueExact(), operation1inResult!!.amount.intValueExact())
        assertEquals(operation1.kind, operation1inResult!!.kind.toString())

        assertEquals(operation2.amount.intValueExact(), operation2inResult!!.amount.intValueExact())
        assertEquals(operation2.kind, operation2inResult!!.kind.toString())
    }

    @Test
    fun getByName_nonExistingAccount_returns() = runBlocking {
        // Given
        val accountName = "Test Account"

        // When
        val result = target!!.getByName(accountName)

        // Then
        assertNull(result)
    }

    @Test
    fun updateAccount_updatesAccountInDb() = runBlocking {
        // Given
        val rankName1 = "test rank 1";
        client!!.insert(DatabaseRank(rankName1, BigDecimal(3)))

        val rankName2 = "test rank 2";
        client!!.insert(DatabaseRank(rankName2, BigDecimal(3)))

        val accountName = "Test Account"
        client!!.insert(DatabaseAccount(accountName, rankName1))

        val account = Account(accountName, rankName1)
        account.rankName = rankName2

        // When
        target!!.updateAccount(account)

        // Then
        val record = (client!!
                selectFrom DatabaseAccounts
                where DatabaseAccounts.name eq accountName
                )
            .fetchOne()
        assertEquals(rankName2, record!!.rankName)

    }


    @BeforeEach
    fun cleanUp() {
        runBlocking {
            database.client.deleteAllFrom(DatabaseOperations)
            database.client.deleteAllFrom(DatabaseAccounts)
            database.client.deleteAllFrom(DatabaseRanks)
        }
    }
}