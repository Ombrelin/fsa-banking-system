package fr.arsenelapostolet.fsa_banking_system.app.database.repositories

import fr.arsenelapostolet.fsa_banking_system.app.database.DatabaseRanks
import fr.arsenelapostolet.fsa_banking_system.app.database.entities.DatabaseRank
import fr.arsenelapostolet.fsa_banking_system.bank.entities.Rank
import fr.arsenelapostolet.fsa_banking_system.bank.persistance.RankRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.ufoss.kotysa.PostgresqlR2dbcSqlClient

class PostgresRankRepository(private val client: PostgresqlR2dbcSqlClient) : RankRepository {
    override suspend fun insert(rank: Rank) {
        client.insert(DatabaseRank(rank))
    }

    override suspend fun getAll(): Collection<Rank> {
        return (client selectFrom DatabaseRanks)
            .fetchAll()
            .map { it.toDomainEntity() }
            .toList()
    }
}