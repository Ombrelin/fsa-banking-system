package fr.arsenelapostolet.fsa_banking_system.app.templates

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Account
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Rank
import kotlinx.html.*
import java.math.BigDecimal
import java.text.DecimalFormat


internal fun BigDecimal.format(): String {
    val df = DecimalFormat()
    df.maximumFractionDigits = 2
    df.minimumFractionDigits = 2
    df.isGroupingUsed = false
    return df.format(this)
}

internal fun FlowContent.accountDetailsPage(account: Account, ranks: Collection<Rank>) {
    div(classes = "center") {
        div(classes = "box") {
            div(classes = "is-flex") {
                div {
                    h1(classes = "title") {
                        +account.name
                    }
                    h2(classes = "subtitle") {
                        +account.rankName
                    }
                }
                span {
                    attributes["id"] = "balance"
                    +account.balance.format()
                }
            }

            br()

            h3("title is-4") {
                +"Operations"
            }

            table(classes = "table is-striped") {
                thead {
                    tr {
                        th {
                            +"Operation Amount"
                        }
                        th {
                            +"Operation Kind"
                        }
                    }
                }
                tbody {
                    account.operations.map {
                        tr(classes = "operation") {
                            td(classes = "amount") {
                                +it.amount.format()
                            }
                            td(classes = "kind") {
                                +it.kind.toString()
                            }
                        }
                    }
                }
            }
            form(method = FormMethod.post, action = "/accounts/${account.name}/operations") {
                div(classes = "is-flex") {
                    div(classes = "select") {
                        select {
                            attributes["name"] = "kind"
                            attributes["required"] = "true"
                            option { +Operation.OperationKind.DEBIT.toString() }
                            option { +Operation.OperationKind.CREDIT.toString() }
                        }
                    }
                    div(classes = "control") {
                        numberInput(name = "amount", classes = "input") {
                            attributes["required"] = "true"
                            attributes["min"] = "0"
                            attributes["step"] = "any"
                        }
                    }
                }

                button(classes = "button is-primary mt-4", type = ButtonType.submit) {
                    +"Add Operation"
                }
            }

            hr()

            h3("title is-4") {
                +"Update Rank"
            }

            form(action = "/accounts/${account.name}/rank", method = FormMethod.post, classes = "is-flex") {
                div(classes = "select") {
                    select {
                        attributes["name"] = "rank"
                        attributes["required"] = "true"
                        ranks.map {
                            option { +it.name }
                        }
                    }
                }
                div(classes = "control") {
                    submitInput(classes = "button is-primary") { value = "Update Rank" }
                }
            }

        }
    }
}