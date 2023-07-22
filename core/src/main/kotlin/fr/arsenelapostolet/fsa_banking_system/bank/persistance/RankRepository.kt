package fr.arsenelapostolet.fsa_banking_system.bank.persistance

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Rank

interface RankRepository {

    suspend fun insert(rank: Rank)
    suspend fun getAll(): Collection<Rank>
}