package fr.arsenelapostolet.fsa_banking_system.app.database.entities

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Account
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Operation

data class DatabaseAccount(val name: String) {

    constructor(domainEntity: Account) : this(domainEntity.name)

    fun toDomainEntity(operations: List<Operation>? = null): Account {
        return Account(name, operations ?: listOf())
    }

}