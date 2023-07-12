package fr.arsenelapostolet.fsa_banking_system.app

import fr.arsenelapostolet.fsa_banking_system.app.database.DatabaseAccounts
import fr.arsenelapostolet.fsa_banking_system.app.database.DatabaseOperations
import fr.arsenelapostolet.fsa_banking_system.app.database.repositories.PostgresAccountRepository
import fr.arsenelapostolet.fsa_banking_system.app.database.repositories.PostgresOperationRepository
import fr.arsenelapostolet.fsa_banking_system.bank.BankApplication
import fr.arsenelapostolet.fsa_banking_system.bank.persistance.AccountRepository
import fr.arsenelapostolet.fsa_banking_system.bank.persistance.OperationRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactoryOptions
import kotlinx.coroutines.runBlocking
import kotlinx.css.CssBuilder
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.kodein.di.ktor.di
import org.kodein.di.singleton
import org.ufoss.kotysa.PostgresqlR2dbcSqlClient
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
                    .coSqlClient(tables().postgresql(DatabaseAccounts, DatabaseOperations))
            }
            bind<AccountRepository> {
                singleton {
                    PostgresAccountRepository(instance())
                }
            }
            bind<OperationRepository> {
                singleton {
                    PostgresOperationRepository(instance())
                }
            }
            bind<BankApplication> {
                singleton {
                    BankApplication(instance(), instance())
                }
            }
        }
        runBlocking {
            val client by closestDI().instance<R2dbcSqlClient>()
            client createTable DatabaseAccounts
            client createTable DatabaseOperations
        }
        configureRouting()
    }.start(wait = true)
}

suspend inline fun ApplicationCall.respondCss(builder: CssBuilder.() -> Unit) {
    this.respondText(CssBuilder().apply(builder).toString(), ContentType.Text.CSS)
}