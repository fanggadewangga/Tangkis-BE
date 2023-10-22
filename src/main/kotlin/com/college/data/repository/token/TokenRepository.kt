package com.college.data.repository.token

interface TokenRepository {
    suspend fun insertToBlacklist(token: String)
    suspend fun isTokenValid(token: String?): Boolean
}