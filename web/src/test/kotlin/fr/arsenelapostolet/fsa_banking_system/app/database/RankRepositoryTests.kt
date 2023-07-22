package fr.arsenelapostolet.fsa_banking_system.app.database

import fr.arsenelapostolet.fsa_banking_system.app.database.entities.DatabaseRank
import fr.arsenelapostolet.fsa_banking_system.app.database.repositories.PostgresRankRepository
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Rank
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.ufoss.kotysa.PostgresqlR2dbcSqlClient
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RankRepositoryTests {

    private var client: PostgresqlR2dbcSqlClient? = null
    private var target: PostgresRankRepository? = null
    private val database = DatabaseFixture()

    @BeforeEach
    fun setup() {
        client = database.client
        target = PostgresRankRepository(client!!)
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
        val rankSalary = BigDecimal(3)

        // When
        target!!.insert(Rank(rankName, rankSalary))

        // Then
        val recordCount = (client!! selectCountFrom DatabaseRanks where DatabaseRanks.name eq rankName)
            .fetchOne()
        assertEquals(1, recordCount)
    }

    @Test
    fun getAll_returnsAllRecordsInDb() = runBlocking {
        // Given
        val records = listOf(
            DatabaseRank("Test rank 1", BigDecimal(1)),
            DatabaseRank("Test rank 2", BigDecimal(2)),
            DatabaseRank("Test rank 3", BigDecimal(3))
        )
        for (record in records) {
            client!!.insert(record)
        }
        // When
        val result = target!!.getAll()

        // Then
        assertEquals(3, result.size)
        val rankNames = result.map { it.name }
        for (record in records) {
            assertTrue(rankNames.contains(record.name))
        }
    }

    @AfterEach
    fun cleanUp() {
        runBlocking {
            database.client.deleteAllFrom(DatabaseOperations)
            database.client.deleteAllFrom(DatabaseAccounts)
            database.client.deleteAllFrom(DatabaseRanks)
        }
    }
}