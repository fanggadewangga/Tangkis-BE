package com.college.di

import com.college.middleware.Middleware
import com.college.security.hashing.HashingService
import com.college.security.hashing.SHA256HashingService
import com.college.security.token.JWTTokenService
import com.college.security.token.TokenService
import org.koin.dsl.module

val securityModule = module {
    single<HashingService> { SHA256HashingService() }
    single<TokenService> { JWTTokenService() }
}

val middlewareModule = module {
    single {
        Middleware(
            get(),
            get(),
            get()
        )
    }
}