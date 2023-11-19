package com.college.middleware

import com.college.data.repository.token.TokenRepository
import com.college.route.RouteResponseHelper.buildErrorJson
import com.college.security.hashing.HashingService
import com.college.security.hashing.SaltedHash
import com.college.security.token.TokenClaim
import com.college.security.token.TokenService
import com.college.utils.Config.tokenConfig
import com.college.utils.Constant
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*

class Middleware(
    private val repository: TokenRepository,
    private val tokenService: TokenService,
    private val hashingService: HashingService
) {

    fun hashPassword(password: String) = hashingService.generateSaltedHash(password)

    suspend fun ApplicationCall.verifyPassword(password: String, salt: SaltedHash) {
        val isPasswordValid = hashingService.verify(password, salt)
        if (!isPasswordValid) {
            buildErrorJson(message = "Password salah!")
        }
    }

    fun generateToken(vararg claims: TokenClaim) = tokenService.generate(
        config = tokenConfig,
        claims = claims
    )

    suspend fun Application.invalidateToken(token: String) {
        tokenService.apply {
            invalidate(token) { repository.insertToBlacklist(this) }
        }
    }

    suspend fun ApplicationCall.validateToken() {
        val jwt = request.header("Authorization")?.substring("Bearer ".length)
        val isValid = repository.isTokenValid(jwt)
        if (!isValid) {
            buildErrorJson(httpStatusCode = HttpStatusCode.Unauthorized, message = "Invalid token")
        }
    }

    private inline fun<reified T: Any> getClaim(call: ApplicationCall, claimKey: String) = kotlin.run {
        val principal = call.principal<JWTPrincipal>()
        principal?.getClaim(claimKey, T::class)
    }
}