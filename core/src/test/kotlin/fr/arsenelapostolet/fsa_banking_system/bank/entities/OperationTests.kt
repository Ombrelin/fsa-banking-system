package fr.arsenelapostolet.fsa_banking_system.bank.entities

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class OperationTests {

    @Test
    fun operation_creation_generatesId(){
        // When
        var operation = Operation(Operation.OperationKind.CREDIT, BigDecimal(18))

        // Then
        assertNotNull(operation.id);
    }

}