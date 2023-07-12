package fr.arsenelapostolet.fsa_banking_system.app.database.entities

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation
import java.math.BigDecimal
import java.util.*

data class DatabaseOperation(
    val id: UUID,
    val kind: String,
    val amount: BigDecimal,
    val accountName: String
) {
    constructor(operation: Operation, accountName: String) : this(
        operation.id,
        operation.kind.toString(),
        operation.amount,
        accountName
    )

    fun toDomainEntity(): Operation {
        return Operation(id, Operation.OperationKind.valueOf(kind), amount)
    }
}
