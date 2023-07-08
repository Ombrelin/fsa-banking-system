package fr.arsenelapostolet.fsa_banking_system.bank

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Account
import org.junit.jupiter.api.Test

class BankApplicationTests {

    val target: BankApplication = BankApplication(FakeAccountRepository())

    @Test
    fun createAccount_insertsRecord(){
        // Given
        val accountName = "test account";

        // When
        target.createAccount(accountName)

        // Then
        
    }

}