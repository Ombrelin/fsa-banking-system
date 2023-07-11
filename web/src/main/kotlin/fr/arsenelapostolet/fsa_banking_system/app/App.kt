package fr.arsenelapostolet.fsa_banking_system.app

import fr.arsenelapostolet.fsa_banking_system.app.database.DatabaseAccounts
import fr.arsenelapostolet.fsa_banking_system.app.database.PostgresAccountRepository
import fr.arsenelapostolet.fsa_banking_system.bank.BankApplication
import fr.arsenelapostolet.fsa_banking_system.bank.persistance.AccountRepository
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactoryOptions
import kotlinx.coroutines.runBlocking
import org.kodein.di.*
import org.kodein.di.ktor.*
import org.ufoss.kotysa.PostgresqlR2dbcSqlClient
import org.ufoss.kotysa.PostgresqlTables
import org.ufoss.kotysa.R2dbcSqlClient
import org.ufoss.kotysa.r2dbc.coSqlClient
import org.ufoss.kotysa.tables

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        di {
            bind<PostgresqlR2dbcSqlClient>() with singleton {
                ConnectionFactories
                    .get(
                        ConnectionFactoryOptions.builder()
                            .option(ConnectionFactoryOptions.DRIVER, "postgresql")
                            .option(ConnectionFactoryOptions.HOST, System.getenv("DB_HOST"))
                            .option(ConnectionFactoryOptions.USER, System.getenv("DB_USERNAME"))
                            .option(ConnectionFactoryOptions.PASSWORD, System.getenv("DB_PASSWORD"))
                            .option(ConnectionFactoryOptions.DATABASE, System.getenv("DB_NAME"))
                            .build()
                    )
                    .coSqlClient(tables().postgresql(DatabaseAccounts))
            }
            bind<AccountRepository> {
                singleton {
                    PostgresAccountRepository(instance())
                }
            }
            bind<BankApplication> {
                singleton {
                    BankApplication(instance())
                }
            }
        }
        runBlocking {
            val client by closestDI().instance<R2dbcSqlClient>()
            client createTable DatabaseAccounts
        }
        configureRouting()
    }.start(wait = true)
}