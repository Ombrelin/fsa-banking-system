package fr.arsenelapostolet.fsa_banking_system.bank.entities

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation.OperationKind
import java.math.BigDecimal

class Account(val name: String, val operations: List<Operation>) {

    constructor(name: String) : this(name, listOf())

    val balance: BigDecimal
        get() = operations.sumOf {
            when (it.kind) {
                OperationKind.CREDIT -> it.amount
                OperationKind.DEBIT -> it.amount.negate()
            }
        }
}