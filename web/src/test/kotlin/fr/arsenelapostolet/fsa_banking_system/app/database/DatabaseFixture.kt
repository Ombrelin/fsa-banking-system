package fr.arsenelapostolet.fsa_banking_system.app.database

import fr.arsenelapostolet.fsa_banking_system.app.database.DatabaseAccounts
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.ConnectionFactoryOptions.*
import org.ufoss.kotysa.PostgresqlR2dbcSqlClient
import org.ufoss.kotysa.r2dbc.coSqlClient
import org.ufoss.kotysa.tables

class DatabaseFixture {

    val client: PostgresqlR2dbcSqlClient = ConnectionFactories
        .get(
            builder()
                .option(DRIVER, "postgresql")
                .option(HOST, "localhost")
                .option(USER, "postgres")
                .option(PASSWORD, "password")
                .option(DATABASE, "fsabank")  // optional
                .build()
        )
        .coSqlClient(tables().postgresql(DatabaseAccounts))

    suspend fun migrate() {
        client createTable DatabaseAccounts
    }

}