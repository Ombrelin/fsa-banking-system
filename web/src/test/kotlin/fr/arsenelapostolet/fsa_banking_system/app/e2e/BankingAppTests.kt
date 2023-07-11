package fr.arsenelapostolet.fsa_banking_system.app.e2e

import com.microsoft.playwright.Page
import fr.arsenelapostolet.fsa_banking_system.app.e2e.pom.AccountsListPageObjectModel
import fr.arsenelapostolet.fsa_banking_system.app.e2e.pom.CreateAccountPageObjectModel
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*
import kotlin.test.assertContains
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BankingAppTests {

    private val serverAddress = System.getenv("APP_URL")

    private var page: Page? = null
    private var playwright: PlaywrightFixture? = null

    @BeforeAll
    fun setup() {
        playwright = PlaywrightFixture()
        page = playwright!!.page;
    }

    @AfterAll
    fun cleanUp() {
        playwright?.close()
    }

    @Test
    fun createAccount_addsAccountToAccountList() {
        // Given
        val testAccountName = "Test Account ${UUID.randomUUID()}"
        val createAccountPageObjectModel = CreateAccountPageObjectModel(page!!, serverAddress)
        createAccountPageObjectModel.goto()

        // When
        createAccountPageObjectModel.createAccount(testAccountName)

        // Then
        val accountListPageObjectModel = AccountsListPageObjectModel(page!!, serverAddress)
        val projects = accountListPageObjectModel.getProjectNames().filter { it == testAccountName }

        assertEquals(1, projects.size)
        assertContains(projects, testAccountName)
    }
}