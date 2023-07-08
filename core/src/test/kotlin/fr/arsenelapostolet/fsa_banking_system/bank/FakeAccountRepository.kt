package fr.arsenelapostolet.fsa_banking_system.bank

import fr.arsenelapostolet.fsa_banking_system.bank.entities.Account
import fr.arsenelapostolet.fsa_banking_system.bank.persistance.AccountRepository

class FakeAccountRepository : AccountRepository {

    val data: MutableMap<String, Account> = HashMap()

    override suspend fun insert(account: Account) {
        data[account.name] = account;
    }

    override suspend fun getByName(accountName: String): Account? = data[accountName]

    override suspend fun getAll(): Collection<Account> = data.values
}