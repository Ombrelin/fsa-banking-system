package fr.arsenelapostolet.fsa_banking_system.app.e2e.pom

import com.microsoft.playwright.Page

abstract class PageObjectModel(val page: Page, val baseAppUrl: String) {

    abstract val path: String

    fun goto() {
        page.navigate("$baseAppUrl$path")
    }

}