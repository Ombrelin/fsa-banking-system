package fr.arsenelapostolet.fsa_banking_system.app.database

import fr.arsenelapostolet.fsa_banking_system.app.database.entities.DatabaseAccount
import fr.arsenelapostolet.fsa_banking_system.app.database.entities.DatabaseOperation
import org.ufoss.kotysa.postgresql.PostgresqlTable

object DatabaseAccounts : PostgresqlTable<DatabaseAccount>("accounts") {
    val name = varchar(DatabaseAccount::name)
        .primaryKey()
}

object DatabaseOperations : PostgresqlTable<DatabaseOperation>("operations") {
    val id = uuid(DatabaseOperation::id)
        .primaryKey()
    val accountname = varchar(DatabaseOperation::accountName)
        .foreignKey(DatabaseAccounts.name)
    val kind = varchar(DatabaseOperation::kind)
    val amount = decimal(DatabaseOperation::amount, 12, 2)
}