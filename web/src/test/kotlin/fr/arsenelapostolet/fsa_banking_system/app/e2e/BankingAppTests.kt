package fr.arsenelapostolet.fsa_banking_system.app.e2e

import com.microsoft.playwright.Page
import fr.arsenelapostolet.fsa_banking_system.app.e2e.pom.AccountDetailPageObjectModel
import fr.arsenelapostolet.fsa_banking_system.app.e2e.pom.AccountsListPageObjectModel
import fr.arsenelapostolet.fsa_banking_system.app.e2e.pom.CreateAccountPageObjectModel
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.math.BigDecimal
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

    @Test
    fun accountDetails_addOperations_displayAccountInfo() {
        // Given
        val testAccountName = "Test Account ${UUID.randomUUID()}"
        val createAccountPageObjectModel = CreateAccountPageObjectModel(page!!, serverAddress)
        createAccountPageObjectModel.goto()
        createAccountPageObjectModel.createAccount(testAccountName)

        val accountDetailsPageObjectModel = AccountDetailPageObjectModel(testAccountName, page!!, serverAddress)
        accountDetailsPageObjectModel.goto()

        // When
        accountDetailsPageObjectModel.addOperation(Operation.OperationKind.CREDIT, 18)
        accountDetailsPageObjectModel.addOperation(Operation.OperationKind.DEBIT, 15)

        // Then
        assertEquals(2, accountDetailsPageObjectModel.operations.size)

        assertTrue(accountDetailsPageObjectModel.operations.map { it.kind }.contains(Operation.OperationKind.CREDIT))
        assertTrue(accountDetailsPageObjectModel.operations.map { it.kind }.contains(Operation.OperationKind.DEBIT))
        assertTrue(accountDetailsPageObjectModel.operations.map { it.amount.toInt() }.contains(18))
        assertTrue(accountDetailsPageObjectModel.operations.map { it.amount.toInt() }.contains(15))

        assertEquals("3.00", accountDetailsPageObjectModel.balance)
        assertEquals(testAccountName, accountDetailsPageObjectModel.pageAccountName)
    }
}