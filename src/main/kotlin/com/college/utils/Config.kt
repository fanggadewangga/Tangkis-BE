package com.college.utils

import com.college.security.token.TokenConfig

object Config {
    val tokenConfig
        get() = TokenConfig(
            issuer = System.getenv("JWT_ISSUER"),
            audience = System.getenv("JWT_AUDIENCE"),
            expiresIn = 365L * 1000L * 60L * 60L * 24L,
            secret = System.getenv("JWT_SECRET")
        )
}