package fr.arsenelapostolet.fsa_banking_system.app.templates

import kotlinx.html.*


internal fun FlowContent.createAccountPage() {
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