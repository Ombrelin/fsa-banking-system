package fr.arsenelapostolet.fsa_banking_system.app.templates

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Account
import kotlinx.html.*

internal fun FlowContent.accountListPage(accounts: Collection<Account>) {
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
                            td("is-flex is-align-content-space-between") {
                                a(href="/accounts/${it.name}"){
                                    button(classes = "button") {
                                        +"View"
                                    }
                                }
                                button(classes = "button is-danger") {
                                    +"Delete"
                                }
                            }
                        }
                    }
                }
            }
            div(classes = "is-flex is-align-content-space-between") {}
            button(classes = "button") {
                +"Pay salaries"
            }
            a(href = "/accounts/create") {
                button(classes = "button is-primary") {
                    +"Create new account"
                }
            }
        }
    }
}