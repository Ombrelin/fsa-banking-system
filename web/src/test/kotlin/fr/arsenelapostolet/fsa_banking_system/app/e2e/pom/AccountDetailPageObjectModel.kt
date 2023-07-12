package fr.arsenelapostolet.fsa_banking_system.app.e2e.pom

import com.microsoft.playwright.Page
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation
import java.math.BigDecimal
import java.util.*

class AccountDetailPageObjectModel(private val accountName: String, page: Page, baseAppUrl: String) :
    PageObjectModel(page, baseAppUrl) {
    fun addOperation(kind: Operation.OperationKind, amount: Int) {
        page.querySelector("""select[ name="kind"]""",).selectOption(kind.toString())
        page.fill("""input[type="number"][ name="amount"]""", amount.toString())
        page.click("button")
    }

    val pageAccountName: String
        get() = page
            .querySelector("h1.title")
            .innerText()
    val balance: String
        get() = page
            .querySelector("#balance")
            .innerText()
    val operations: List<Operation>
        get() = page
            .querySelectorAll("tr.operation")
            .map {
                Operation(
                    UUID.randomUUID(),
                    Operation.OperationKind.valueOf(it.querySelector("td.kind").innerText()),
                    BigDecimal(it.querySelector("td.amount").innerText())
                )
            }.toList()
    override val path: String
        get() = "/accounts/$accountName"

}