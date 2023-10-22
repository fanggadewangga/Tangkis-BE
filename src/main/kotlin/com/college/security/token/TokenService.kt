package com.college.security.token

import io.ktor.server.application.*

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String

    suspend fun Application.invalidate(token: String, saveToDb: suspend String.() -> Unit)
}