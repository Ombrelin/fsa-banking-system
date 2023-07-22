package fr.arsenelapostolet.fsa_banking_system.bank

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Rank
import fr.arsenelapostolet.fsa_banking_system.bank.persistance.RankRepository

class FakeRankRepository : RankRepository {

    val data: MutableMap<String, Rank> = HashMap()

    override suspend fun insert(rank: Rank) {
        data[rank.name] = rank
    }

    override suspend fun getAll(): Collection<Rank> {
        return data.values
    }
}