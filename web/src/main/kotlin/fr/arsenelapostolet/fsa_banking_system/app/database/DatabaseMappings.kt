package fr.arsenelapostolet.fsa_banking_system.app.database

import fr.arsenelapostolet.fsa_banking_system.app.database.entities.DatabaseAccount
import fr.arsenelapostolet.fsa_banking_system.app.database.entities.DatabaseOperation
import fr.arsenelapostolet.fsa_banking_system.app.database.entities.DatabaseRank
import org.ufoss.kotysa.postgresql.PostgresqlTable

object DatabaseRanks : PostgresqlTable<DatabaseRank>("ranks"){
    val name = varchar(DatabaseRank::name)
        .primaryKey()
    val salary = decimal(DatabaseRank::salary, 12, 2)
}

object DatabaseAccounts : PostgresqlTable<DatabaseAccount>("accounts") {
    val name = varchar(DatabaseAccount::name)
        .primaryKey()
    val rankname = varchar(DatabaseAccount::rankName)
        .foreignKey(DatabaseRanks.name)
}

object DatabaseOperations : PostgresqlTable<DatabaseOperation>("operations") {
    val id = uuid(DatabaseOperation::id)
        .primaryKey()
    val accountname = varchar(DatabaseOperation::accountName)
        .foreignKey(DatabaseAccounts.name)
    val kind = varchar(DatabaseOperation::kind)
    val amount = decimal(DatabaseOperation::amount, 12, 2)
}

