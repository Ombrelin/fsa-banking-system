package fr.arsenelapostolet.fsa_banking_system.app.database.entities

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Account

data class DatabaseAccount(val name: String) {

    constructor(domainEntity: Account) : this(domainEntity.name)

    fun toDomainEntity(): Account {
        return Account(name)
    }

}