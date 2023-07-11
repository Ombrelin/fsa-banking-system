package fr.arsenelapostolet.fsa_banking_system.app.e2e.pom

import com.microsoft.playwright.Page

class AccountsListPageObjectModel(page: Page, baseAppUrl: String) : PageObjectModel(page, baseAppUrl) {
    fun getProjectNames(): List<String> {
        return page.querySelectorAll("td:first-child").map { it.innerText() }

    }

    override val path: String
        get() = "/accounts"
}