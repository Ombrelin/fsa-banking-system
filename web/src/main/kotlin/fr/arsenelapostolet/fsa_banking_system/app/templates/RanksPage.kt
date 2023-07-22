package fr.arsenelapostolet.fsa_banking_system.app.templates

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Rank
import kotlinx.html.*

internal fun FlowContent.ranksPage(ranks: Collection<Rank>) {
    div(classes = "center") {
        div(classes = "box") {
            h1(classes = "title") {
                +"Ranks"
            }
            table(classes = "table is-striped") {
                thead {
                    tr {
                        th {
                            +"Ranks Name"
                        }
                        th {
                            +"Salary"
                        }
                    }
                }
                tbody {
                    ranks.map {
                        tr {
                            td { +it.name }
                            td {
                                +it.salary.format()
                            }
                        }
                    }
                }
            }

            hr()

            form(action = "/ranks/create", method = FormMethod.post) {
                h1(classes = "title") {
                    +"Create new Rank"
                }

                div(classes = "field") {
                    label(classes = "label") {
                        +"Rank Name :"
                    }
                    div(classes = "control") {
                        textInput(name = "rankName", classes = "input") {
                            attributes["required"] = "true"
                        }
                    }
                }

                div(classes = "field") {
                    label(classes = "label") {
                        +"Rank Salary :"
                    }
                    div(classes = "control") {
                        numberInput(name = "rankSalary", classes = "input") {
                            attributes["required"] = "true"
                        }
                    }
                }

                div(classes = "control") {
                    submitInput(classes = "button is-primary") { value = "Create Rank" }
                }
            }

        }
    }
}