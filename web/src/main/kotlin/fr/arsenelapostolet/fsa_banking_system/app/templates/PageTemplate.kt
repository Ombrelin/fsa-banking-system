package fr.arsenelapostolet.fsa_banking_system.app.templates

import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.HTML
import kotlinx.html.*

class PageTemplate : Template<HTML> {
    val content = Placeholder<FlowContent>()
    override fun HTML.apply() {
        head {
            title("FSA Bank")
            link(rel = "stylesheet", href = "https://cdn.jsdelivr.net/npm/bulma@0.9.4/css/bulma.min.css")
            link(rel = "stylesheet", href = "/static/style.css")
        }
        body {
            insert(content)
        }
    }
}