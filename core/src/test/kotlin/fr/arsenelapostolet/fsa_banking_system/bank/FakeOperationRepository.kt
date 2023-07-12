package fr.arsenelapostolet.fsa_banking_system.bank

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation
import fr.arsenelapostolet.fsa_banking_system.bank.persistance.OperationRepository
import java.util.*

class FakeOperationRepository : OperationRepository {
    val data: MutableMap<UUID, Pair<Operation, String>> = HashMap()


    override suspend fun insert(operation: Operation, accountName: String) {
        data[operation.id] = Pair(operation, accountName)
    }
}