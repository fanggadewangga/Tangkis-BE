package com.college.di

import com.college.data.database.DatabaseFactory
import com.college.data.repository.consultation.ConsultationRepository
import com.college.data.repository.consultation.ConsultationRepositoryImpl
import com.college.data.repository.consultation_time.ConsultationTimeRepository
import com.college.data.repository.consultation_time.ConsultationTimeRepositoryImpl
import com.college.data.repository.contact.ContactRepository
import com.college.data.repository.contact.ContactRepositoryImpl
import com.college.data.repository.report.ReportRepository
import com.college.data.repository.report.ReportRepositoryImpl
import com.college.data.repository.token.TokenRepository
import com.college.data.repository.token.TokenRepositoryImpl
import com.college.data.repository.user.UserRepository
import com.college.data.repository.user.UserRepositoryImpl
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.koin.dsl.module
import java.net.URI

val databaseModule = module {
    single {
        DatabaseFactory(get())
    }

    factory {
        val config = HikariConfig()
        config.apply {
            driverClassName = System.getenv("JDBC_DRIVER")
            maximumPoolSize = 6
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"


            jdbcUrl = if (System.getenv("ENV") == "DEV")
                System.getenv("DATABASE_URL")
            else {
                val uri = URI(System.getenv("DATABASE_URL"))
                val username = uri.userInfo.split(":").toTypedArray()[0]
                val password = uri.userInfo.split(":").toTypedArray()[1]
                "jdbc:postgresql://${uri.host}:${uri.port}${uri.path}?sslmode=require&user=$username&password=$password"
            }
            validate()
        }
        HikariDataSource(config)
    }
}

val repositoryModule = module {
    single<UserRepository> {
        UserRepositoryImpl(get())
    }

    single<TokenRepository> {
        TokenRepositoryImpl(get())
    }

    single<ContactRepository> {
        ContactRepositoryImpl(get())
    }

    single<ReportRepository> {
        ReportRepositoryImpl(get())
    }

    single<ConsultationRepository> {
        ConsultationRepositoryImpl(get())
    }

    single<ConsultationTimeRepository> {
        ConsultationTimeRepositoryImpl(get())
    }
}