package fr.arsenelapostolet.fsa_banking_system.bank

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Account
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Rank
import fr.arsenelapostolet.fsa_banking_system.bank.persistance.RankRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class BankApplicationTests {

    private val accountRepository = FakeAccountRepository()
    private val operationRepository = FakeOperationRepository()
    private val rankRepository = FakeRankRepository()
    private val target: BankApplication = BankApplication(accountRepository, operationRepository, rankRepository)

    @Test
    fun createAccount_insertsRecord() = runBlocking {
        // Given
        val accountName = "test account"
        val rankName = "test rank"

        // When
        val result = target.createAccount(accountName, rankName)

        // Then
        assertEquals(accountName, result.name)
        assertEquals(accountName, accountRepository.data[accountName]!!.name)
        assertEquals(rankName, accountRepository.data[accountName]!!.rankName)
    }

    @Test
    fun listAccounts_returnsAccountsInDb() = runBlocking {
        // Given
        val accounts = listOf(
            Account("Test account 1", "test rank"),
            Account("Test account 2", "test rank"),
            Account("Test account 3", "test rank")
        )
        for (account in accounts) {
            accountRepository.data[account.name] = account
        }

        // When
        val result = target.listAccounts()

        // Then
        assertEquals(3, result.size)
        for (account in accounts) {
            assertTrue(result.contains(account))
        }

    }

    @Test
    fun accountDetails_existingAccount_returnsAccount() = runBlocking {
        // Given
        val account = Account("Test Account", "test rank")
        accountRepository.data[account.name] = account;

        // When
        val result = target.accountDetails(account.name);

        // Then
        assertEquals(account, result)
    }

    @Test
    fun accountDetails_nonExistingAccount_returnsNull() = runBlocking {
        // Given
        val accountName = "Non existing account";

        // When
        val result = target.accountDetails(accountName);

        // Then
        assertNull(result)
    }

    @Test
    fun addOperation_insertsOperationInDb() = runBlocking {
        // Given
        val accountName = "Account Name"
        val operationAmount = BigDecimal(18)

        // When

        val result = target.addOperation(
            Operation.OperationKind.CREDIT,
            operationAmount,
            accountName
        )

        // Then
        assertEquals(1, operationRepository.data.keys.size)
        val (operation, operationAccountName) = operationRepository.data.values.single()
        assertEquals(operationAmount, operation.amount)
        assertEquals(accountName, operationAccountName)
        assertEquals(Operation.OperationKind.CREDIT, operation.kind)

        assertEquals(operationAmount, result.amount)
        assertEquals(Operation.OperationKind.CREDIT, result.kind)
    }

    @Test
    fun createRank_insertsRankInDb() = runBlocking {
        // Given
        val rankName = "Rank Name"
        val salary = BigDecimal(18)

        // When
        val result = target.createRank(rankName, salary)

        // Then
        assertEquals(result, rankRepository.data[rankName])
        assertEquals(rankName, rankRepository.data[rankName]!!.name)
        assertEquals(salary, rankRepository.data[rankName]!!.salary)
    }

    @Test
    fun listRanks_getAllRanksInDb() = runBlocking() {
        // Given
        val ranks = listOf(
            Rank("Test rank 1", BigDecimal(1)),
            Rank("Test rank 2", BigDecimal(2)),
            Rank("Test rank 3", BigDecimal(3))
        )
        for (rank in ranks) {
            rankRepository.data[rank.name] = rank
        }

        // When
        val result = target.listRanks()

        // Then
        assertEquals(3, result.size)
        for (rank in ranks) {
            assertTrue(result.contains(rank))
        }

    }

    @Test
    fun promoteAccount_updateRankOfAccount() = runBlocking {
        // Given
        val account = Account("Test Account", "test rank")
        accountRepository.data[account.name] = account
        val newRankName = "New Test Rank"

        // When
        target.promoteAccount(account.name, newRankName)

        // Then
        assertEquals(newRankName, accountRepository.data.values.single().rankName)
    }

    @Test
    fun paySalaries_createsNewOpWithSalaryForEachAccount() = runBlocking {
        // Given
        val ranks = listOf(
            Rank("Test rank 1", BigDecimal(1)),
            Rank("Test rank 2", BigDecimal(2)),
            Rank("Test rank 3", BigDecimal(3))
        )
        for (rank in ranks) {
            rankRepository.data[rank.name] = rank
        }
        val accounts = listOf(
            Account("Test account 1", ranks[0].name),
            Account("Test account 2", ranks[1].name),
            Account("Test account 3", ranks[2].name)
        )
        for (account in accounts) {
            accountRepository.data[account.name] = account
        }

        // When
        target.paySalaries()

        // Then
        assertEquals(3, operationRepository.data.size)
        assertEquals(
            BigDecimal(1),
            operationRepository.data.values.single { it.second == accounts[0].name }.first.amount
        )
        assertEquals(
            Operation.OperationKind.CREDIT,
            operationRepository.data.values.single { it.second == accounts[0].name }.first.kind
        )
        assertEquals(
            BigDecimal(2),
            operationRepository.data.values.single { it.second == accounts[1].name }.first.amount
        )
        assertEquals(
            Operation.OperationKind.CREDIT,
            operationRepository.data.values.single { it.second == accounts[1].name }.first.kind
        )
        assertEquals(
            BigDecimal(3),
            operationRepository.data.values.single { it.second == accounts[2].name }.first.amount
        )
        assertEquals(
            Operation.OperationKind.CREDIT,
            operationRepository.data.values.single { it.second == accounts[2].name }.first.kind
        )

    }
}