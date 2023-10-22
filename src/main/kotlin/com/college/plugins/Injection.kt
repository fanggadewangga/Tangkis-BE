package com.college.plugins

import com.college.di.*
import io.ktor.server.application.*
import org.koin.core.logger.Level
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureInjection() {
    install(Koin) {
        slf4jLogger(Level.ERROR)
        modules(
            databaseModule,
            repositoryModule,
            securityModule,
            middlewareModule,
            routeModule
        )
    }
}