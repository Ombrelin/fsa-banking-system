package fr.arsenelapostolet.fsa_banking_system.bank.entities

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class AccountTests {

    @Test
    fun balance_computesSumOfOperations() {
        // Given
        var account = Account(
            "Test Account",
            listOf(
                Operation(Operation.OperationKind.CREDIT, BigDecimal(10)),
                Operation(Operation.OperationKind.DEBIT, BigDecimal(5.5)),
                Operation(Operation.OperationKind.CREDIT, BigDecimal(15)),
                Operation(Operation.OperationKind.CREDIT, BigDecimal(2)),
                Operation(Operation.OperationKind.DEBIT, BigDecimal(3))
            )
        );

        // When
        var result = account.balance;

        // Then
        assertEquals(BigDecimal(18.5), result)
    }

}