package fr.arsenelapostolet.fsa_banking_system.bank.entities

import java.math.BigDecimal
import java.util.*

class Operation(
    val id: UUID,
    val kind: OperationKind,
    val amount: BigDecimal
) {

    constructor(kind: OperationKind, amount: BigDecimal) : this(UUID.randomUUID(), kind, amount)

    enum class OperationKind {
        CREDIT, DEBIT
    }
}