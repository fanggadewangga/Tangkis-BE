package com.college.data.repository.token

import com.college.data.database.DatabaseFactory
import com.college.data.table.TokenBlacklistTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class TokenRepositoryImpl(private val dbFactory: DatabaseFactory) : TokenRepository {
    override suspend fun insertToBlacklist(token: String) {
        dbFactory.dbQuery {
            TokenBlacklistTable.insert {
                it[TokenBlacklistTable.token] = token
            }
        }
    }

    override suspend fun isTokenValid(token: String?): Boolean = dbFactory.dbQuery {
        if (token == null) return@dbQuery false
        TokenBlacklistTable.select {
            TokenBlacklistTable.token eq token
        }.empty()
    }
}