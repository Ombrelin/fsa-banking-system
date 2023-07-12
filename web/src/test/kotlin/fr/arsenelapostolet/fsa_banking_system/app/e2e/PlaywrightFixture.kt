package fr.arsenelapostolet.fsa_banking_system.app.e2e

import com.microsoft.playwright.*
import io.ktor.utils.io.core.*
import java.nio.file.Paths


class PlaywrightFixture : Closeable {
    private var playwright: Playwright? = null
    private var browser: Browser? = null
    private var context: BrowserContext? = null

    var page: Page? = null

    constructor() {
        playwright = Playwright.create()
        val isContinuousIntegration = (System.getenv("IS_CI") ?: false.toString()).toBoolean()
        val browserTypeLaunchOptions = if (isContinuousIntegration)
            BrowserType.LaunchOptions().setHeadless(true)
        else BrowserType.LaunchOptions().setHeadless(false).setSlowMo(500.0)
        browser = playwright!!.firefox().launch(browserTypeLaunchOptions)

        if (!isContinuousIntegration) {
            context = browser!!.newContext(
                Browser.NewContextOptions()
                    .setRecordVideoDir(Paths.get("videos"))
            )
            page = context!!.newPage()
        } else {
            page = browser!!.newPage()
        }


    }

    override fun close() {
        context?.close()
        browser?.close()
        playwright?.close()
    }
}