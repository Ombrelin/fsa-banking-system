package fr.arsenelapostolet.fsa_banking_system.app.templates

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Account
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation
import kotlinx.html.*
import java.math.BigDecimal
import java.text.DecimalFormat


private fun BigDecimal.format(): String {
    val df = DecimalFormat()
    df.maximumFractionDigits = 2
    df.minimumFractionDigits = 2
    df.isGroupingUsed = false
    return df.format(this)
}

internal fun FlowContent.accountDetailsPage(account: Account) {
    div(classes = "center") {
        div(classes = "box") {
            div(classes = "is-flex") {
                h1(classes = "title") {
                    +account.name
                }
                span {
                    attributes["id"] = "balance"
                    +account.balance.format()
                }
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
                        tr(classes="operation") {
                            td (classes = "amount"){
                                +it.amount.format()
                            }
                            td (classes = "kind"){
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
                            option { +Operation.OperationKind.DEBIT.toString() }
                            option { +Operation.OperationKind.CREDIT.toString() }
                        }
                    }
                    div(classes = "control") {
                        numberInput(name = "amount", classes = "input") {
                            attributes["min"] = "0"
                            attributes["step"] = "any"
                        }
                    }
                }

                button(classes = "button is-primary mt-4", type = ButtonType.submit) {
                    +"Add Operation"
                }
            }
        }
    }
}