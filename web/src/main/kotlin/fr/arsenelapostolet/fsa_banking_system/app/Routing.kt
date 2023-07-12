package fr.arsenelapostolet.fsa_banking_system.app

import fr.arsenelapostolet.fsa_banking_system.app.templates.*
import fr.arsenelapostolet.fsa_banking_system.bank.BankApplication
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.body
import kotlinx.html.p
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import java.math.BigDecimal

fun Application.configureRouting() {
    routing {
        get("/style.css") {
            call.respondCss {
                style()
            }
        }
        get("/accounts/{accountName}") {
            val bankingApp by closestDI().instance<BankApplication>()
            val accountName = call.parameters["accountName"]

            if (accountName == null) {
                call.respondHtml(HttpStatusCode.BadRequest) {
                    body {
                        p {
                            "accountName is required"
                        }
                    }
                }
            }

            val account = bankingApp.accountDetails(accountName!!)

            if (account == null) {
                call.respondHtml(HttpStatusCode.NotFound) {
                    body {
                        p {
                            "account does not exist"
                        }
                    }
                }
            }


            call.respondHtmlTemplate(PageTemplate(), HttpStatusCode.OK) {
                content {
                    accountDetailsPage(account!!)
                }
            }
        }
        post("/accounts/{accountName}/operations") {
            val formParameters = call.receiveParameters()
            val bankingApp by closestDI().instance<BankApplication>()
            val accountName = call.parameters["accountName"]

            bankingApp.addOperation(
                Operation.OperationKind.valueOf(formParameters["kind"]!!),
                BigDecimal(formParameters["amount"]),
                accountName!!
            )

            call.respondRedirect("/accounts/$accountName", permanent = true)
        }
        get("/accounts") {
            val bankingApp by closestDI().instance<BankApplication>()
            val accounts = bankingApp.listAccounts()

            call.respondHtmlTemplate(PageTemplate(), HttpStatusCode.OK) {
                content {
                    accountListPage(accounts)
                }
            }
        }
        get("/accounts/create") {
            call.respondHtmlTemplate(PageTemplate(), HttpStatusCode.OK) {
                content {
                    createAccountPage()
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
            } else {
                bankingApp.createAccount(holderName!!)
                call.respondRedirect("/accounts", permanent = true)
            }
        }
    }
}


