package fr.arsenelapostolet.fsa_banking_system.bank.persistance

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Account


interface AccountRepository {

    suspend fun insert(account: Account)
    suspend fun getByName(accountName: String): Account?
    suspend fun getAll(): Collection<Account>
    suspend fun updateAccount(account: Account)

}