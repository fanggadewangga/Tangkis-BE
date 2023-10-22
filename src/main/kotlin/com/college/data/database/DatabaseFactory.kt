package com.college.data.database

import com.college.data.table.*
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseFactory(
    private val dataSource: HikariDataSource
) {

    init {
        Database.connect(dataSource)
        transaction {
            val tables = listOf(
                UserTable, ContactTable,ReportTable, ConsultationTable, ConsultationTimeTable
            )
            tables.forEach { table ->
                SchemaUtils.create(table)
                SchemaUtils.createMissingTablesAndColumns(table)
                SchemaUtils.addMissingColumnsStatements(table)
            }
        }
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}