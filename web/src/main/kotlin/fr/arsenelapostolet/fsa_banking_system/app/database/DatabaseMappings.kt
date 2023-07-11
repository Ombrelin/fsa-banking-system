package fr.arsenelapostolet.fsa_banking_system.app.database

import fr.arsenelapostolet.fsa_banking_system.app.database.entities.DatabaseAccount
import org.ufoss.kotysa.postgresql.PostgresqlTable

object DatabaseAccounts : PostgresqlTable<DatabaseAccount>("accounts") {
    val name = varchar(DatabaseAccount::name)
        .primaryKey()
}