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
                            td(classes = "is-flex is-justify-content-space-between") {
                                div {
                                    a(href = "/accounts/${it.name}") {
                                        button(classes = "button") {
                                            +"View"
                                        }
                                    }
                                }
                                div {
                                    button(classes = "button is-danger") {
                                        +"Delete"
                                    }
                                }
                            }
                        }
                    }
                }
            }
            div(classes = "columns is-mobile is-flex-wrap-wrap") {
                div(classes = "column") {
                    form(action = "/accounts/paySalaries", method = FormMethod.post) {
                        button(classes = "button", type = ButtonType.submit) {
                            +"Pay salaries"
                        }
                    }
                }

                div(classes = "column") {
                    a(href = "/ranks") {
                        button(classes = "button") {
                            +"Manage Ranks"
                        }
                    }
                }
                div(classes = "column") {
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