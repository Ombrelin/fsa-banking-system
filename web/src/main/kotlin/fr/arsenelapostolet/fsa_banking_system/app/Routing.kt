package fr.arsenelapostolet.fsa_banking_system.app

import fr.arsenelapostolet.fsa_banking_system.app.templates.PageTemplate
import fr.arsenelapostolet.fsa_banking_system.bank.BankApplication
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import kotlinx.html.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.configureRouting() {
    routing {
        staticResources("/static", ".")
        get("/accounts") {
            val bankingApp by closestDI().instance<BankApplication>()
            val accounts = bankingApp.listAccounts()

            call.respondHtmlTemplate(PageTemplate(), HttpStatusCode.OK) {
                content {
                    div(classes = "center") {
                        div(classes = "box") {
                            h1(classes = "title") {
                                +"Accounts"
                            }
                            table(classes = "table is-striped") {
                                thead {
                                    tr {
                                        th {
                                            +"Account Name"
                                        }
                                        th {
                                            +"Actions"
                                        }
                                    }
                                }
                                tbody {
                                    accounts.map {
                                        tr {
                                            td { +it.name }
                                            td("is-flex") {
                                                button(classes = "button") {
                                                    +"View"
                                                }
                                                button(classes = "button is-danger") {
                                                    +"Delete"
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            button (classes = "button"){
                                + "Pay salaries"
                            }
                            a(href = "/accounts/create") {
                                button(classes = "button is-primary") {
                                    +"Create new account"
                                }
                            }
                        }
                    }
                }
            }
        }
        get("/accounts/create") {
            call.respondHtmlTemplate(PageTemplate(), HttpStatusCode.OK) {
                content {
                    div(classes = "center") {

                        form(action = "/accounts/create", method = FormMethod.post, classes = "box") {
                            h1(classes = "title") {
                                +"Create new Account"
                            }
                            div(classes = "field") {
                                label(classes = "label") {
                                    +"Holder Account Name :"
                                }
                                div(classes = "control") {
                                    textInput(name = "holderName", classes = "input")
                                }

                            }
                            div(classes = "control") {
                                submitInput(classes = "button is-primary") { value = "Create Account" }
                            }
                        }
                    }
                }
            }
        }
        post("/accounts/create") {
            val formParameters = call.receiveParameters()
            val bankingApp by closestDI().instance<BankApplication>()

            val holderName = formParameters["holderName"]
            if (holderName == null) {
                call.respondHtml(HttpStatusCode.BadRequest) {
                    body {
                        p {
                            "holderName is required"
                        }
                    }
                }
            }

            bankingApp.createAccount(holderName!!)
            call.respondRedirect("/accounts", permanent = true)
        }
    }
}