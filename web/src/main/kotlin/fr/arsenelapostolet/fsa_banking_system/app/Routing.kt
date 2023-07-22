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
        post("/accounts/paySalaries"){
            val bankingApp by closestDI().instance<BankApplication>()

            bankingApp.paySalaries()
            call.respondRedirect("/accounts", permanent = true)
        }
        post("/accounts/{accountName}/rank"){
            val accountName = call.parameters["accountName"]
            val formParameters = call.receiveParameters()
            val bankingApp by closestDI().instance<BankApplication>()
            val rank = formParameters["rank"]

            if (accountName == null || rank == null ) {
                call.respondHtml(HttpStatusCode.BadRequest) {
                    body {
                        p {
                            "accountName and rank are required"
                        }
                    }
                }
            }
            else {
                bankingApp.promoteAccount(accountName, rank)
                call.respondRedirect("/accounts/${accountName}", permanent = true)
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

            val ranks = bankingApp.listRanks()

            call.respondHtmlTemplate(PageTemplate(), HttpStatusCode.OK) {
                content {
                    accountDetailsPage(account!!, ranks)
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
            val bankingApp by closestDI().instance<BankApplication>()
            val ranks = bankingApp.listRanks()

            call.respondHtmlTemplate(PageTemplate(), HttpStatusCode.OK) {
                content {
                    createAccountPage(ranks)
                }
            }
        }
        post("/accounts/create") {
            val formParameters = call.receiveParameters()
            val bankingApp by closestDI().instance<BankApplication>()

            val holderName = formParameters["holderName"]
            val rank = formParameters["rank"]
            if (holderName == null || rank == null) {
                call.respondHtml(HttpStatusCode.BadRequest) {
                    body {
                        p {
                            "holderName and rank are required"
                        }
                    }
                }
            } else {
                bankingApp.createAccount(holderName, rank)
                call.respondRedirect("/accounts", permanent = true)
            }
        }
        get("/ranks") {
            val bankingApp by closestDI().instance<BankApplication>()
            val ranks = bankingApp.listRanks()

            call.respondHtmlTemplate(PageTemplate(), HttpStatusCode.OK) {
                content {
                    ranksPage(ranks)
                }
            }
        }
        post("/ranks/create") {
            val formParameters = call.receiveParameters()
            val bankingApp by closestDI().instance<BankApplication>()

            val rankName = formParameters["rankName"]
            val rankSalary = formParameters["rankSalary"]

            if (rankName == null || rankSalary == null) {
                call.respondHtml(HttpStatusCode.BadRequest) {
                    body {
                        p {
                            "rankName and rankSalary are required"
                        }
                    }
                }
            } else {
                bankingApp.createRank(rankName, BigDecimal(rankSalary))
                call.respondRedirect("/ranks", permanent = true)
            }
        }
    }
}


