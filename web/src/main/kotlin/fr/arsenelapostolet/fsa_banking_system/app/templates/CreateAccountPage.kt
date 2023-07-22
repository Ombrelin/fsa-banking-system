package fr.arsenelapostolet.fsa_banking_system.app.templates

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Rank
import kotlinx.html.*


internal fun FlowContent.createAccountPage(ranks: Collection<Rank>) {
    div(classes = "center") {
        form(action = "/accounts/create", method = FormMethod.post, classes = "box") {
            h1(classes = "title") {
                +"Create new Account"
            }

            div(classes = "select") {
                select {
                    attributes["name"] = "rank"
                    attributes["required"] = "true"
                    ranks.map {
                        option { +it.name }
                    }
                }
            }

            div(classes = "field") {
                label(classes = "label") {
                    +"Holder Account Name :"
                }
                div(classes = "control") {
                    textInput(name = "holderName", classes = "input"){
                        attributes["required"] = "true"
                    }
                }

            }
            div(classes = "control") {
                submitInput(classes = "button is-primary") { value = "Create Account" }
            }
        }
    }
}