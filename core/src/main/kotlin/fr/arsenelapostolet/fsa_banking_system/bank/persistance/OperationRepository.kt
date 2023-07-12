package fr.arsenelapostolet.fsa_banking_system.bank.persistance

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation

interface OperationRepository {

    suspend fun insert(operation: Operation, accountName: String)

}