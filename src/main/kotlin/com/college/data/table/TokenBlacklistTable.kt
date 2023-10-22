package com.college.data.table

import org.jetbrains.exposed.sql.Table

object TokenBlacklistTable: Table() {
    override val tableName: String
        get() = "token_blacklist"

    val token = varchar("token", 1024)
}