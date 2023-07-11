package fr.arsenelapostolet.fsa_banking_system.bank

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Account
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class BankApplicationTests {

    private val repository = FakeAccountRepository()
    private val target: BankApplication = BankApplication(repository)

    @Test
    fun createAccount_insertsRecord() = runBlocking {
        // Given
        val accountName = "test account";

        // When
        val result = target.createAccount(accountName)

        // Then
        assertEquals(accountName, result.name)
        assertEquals(accountName, repository.data[accountName]!!.name)
    }

    @Test
    fun listAccounts_returnsAccountsInDb() = runBlocking {
        // Given
        val accounts = listOf(
            Account("Test account 1"),
            Account("Test account 2"),
            Account("Test account 3")
        )
        for(account in accounts){
            repository.data[account.name] = account
        }

        // When
        val result = target.listAccounts()

        // Then
        assertEquals(3, result.size)
        for(account in accounts){
            assertTrue(result.contains(account))
        }

    }

}