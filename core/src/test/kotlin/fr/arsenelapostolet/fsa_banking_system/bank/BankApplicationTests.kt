package fr.arsenelapostolet.fsa_banking_system.bank

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Account
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class BankApplicationTests {

    private val accountRepository = FakeAccountRepository()
    private val operationRepository = FakeOperationRepository()
    private val target: BankApplication = BankApplication(accountRepository, operationRepository)

    @Test
    fun createAccount_insertsRecord() = runBlocking {
        // Given
        val accountName = "test account";

        // When
        val result = target.createAccount(accountName)

        // Then
        assertEquals(accountName, result.name)
        assertEquals(accountName, accountRepository.data[accountName]!!.name)
    }

    @Test
    fun listAccounts_returnsAccountsInDb() = runBlocking {
        // Given
        val accounts = listOf(
            Account("Test account 1"),
            Account("Test account 2"),
            Account("Test account 3")
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
        var account = Account("Test Account")
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

        var result = target.addOperation(
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
}