package fr.arsenelapostolet.fsa_banking_system.app.templates

import kotlinx.css.*

internal fun CssBuilder.style() {
    rule(".center") {
        display = Display.flex
        justifyContent = JustifyContent.center
        alignContent = Align.center

    }
    rule(".box") {
        marginTop = 4.rem
        marginBottom = 4.rem
    }
    rule("#balance") {
        fontSize = 48.px
    }
    table {
        width = 100.pct
    }

    media("only screen and (max-width: 600px)") {
        rule(".center") {
            display = Display.flex
            height = LinearDimension.auto
        }

        rule(".box") {
            put("margin-top", "1.25rem !important")
            put("margin-bottom", "1.25rem !important")
            width = 90.pct
        }


        rule("td:first-child") {
            maxWidth = 0.px
            overflow = Overflow.hidden
            textOverflow = TextOverflow.ellipsis
            whiteSpace = WhiteSpace.nowrap
        }


    }
}
