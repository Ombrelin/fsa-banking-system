package fr.arsenelapostolet.fsa_banking_system.app.e2e.pom

import com.microsoft.playwright.Page

class CreateAccountPageObjectModel(page: Page, baseAppUrl: String) : PageObjectModel(page, baseAppUrl) {
    override val path: String
        get() = "/accounts/create"

    fun createAccount(name: String){
        page.fill("""input[type="text"][ name="holderName"]""", name)
        page.click("""input[type="submit"]""")
    }
}