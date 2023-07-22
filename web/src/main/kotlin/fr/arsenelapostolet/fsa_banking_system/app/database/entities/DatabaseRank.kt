package fr.arsenelapostolet.fsa_banking_system.app.database.entities

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Rank
import java.math.BigDecimal

data class DatabaseRank(val name: String, val salary: BigDecimal) {

    constructor(rank: Rank) : this(rank.name, rank.salary)

    fun toDomainEntity() : Rank = Rank(name, salary)

}
