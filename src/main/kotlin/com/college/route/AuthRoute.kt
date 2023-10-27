package com.college.route

import com.college.data.repository.user.UserRepository
import com.college.middleware.Middleware
import com.college.model.request.user.UserLoginRequest
import com.college.model.request.user.UserRegisterRequest
import com.college.model.response.token.TokenResponse
import com.college.route.RouteResponseHelper.buildErrorJson
import com.college.route.RouteResponseHelper.buildSuccessJson
import com.college.security.hashing.SaltedHash
import com.college.security.token.TokenClaim
import com.college.utils.Constant
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

class AuthRoute(
    private val repository: UserRepository,
    private val middleware: Middleware
) {
    private fun Route.signUp() {
        post("/register") {
            val body = call.receive<UserRegisterRequest>()
            val saltedHash = middleware.hashPassword(body.password)
            val isIdentityNumberExist = repository.isIdentityNumberExist(body.nim)
            val areFieldsBlank = body.name.isBlank() || body.password.isBlank() || body.nim.isBlank()
            val isPasswordTooSort = body.password.length < 8
            val isFilkomStudent = Regex("""^\d{2}515\d*$""").matches(body.nim)

            if (isIdentityNumberExist) {
                call.buildErrorJson(message = "NIM telah terdaftar", httpStatusCode = HttpStatusCode.Conflict)
                return@post
            } else if (areFieldsBlank || isPasswordTooSort) {
                call.buildErrorJson(
                    message = "invalid email or password must be longer than 6 characters",
                    httpStatusCode = HttpStatusCode.BadRequest
                )
                return@post
            } else if (!isFilkomStudent) {
                call.buildErrorJson(
                    message = "NIM tidak terdaftar sebagai mahasiswa FILKOM!",
                    httpStatusCode = HttpStatusCode.Conflict
                )
                return@post
            }

            val user = repository.insertUser(body, saltedHash)
            val token = middleware.generateToken(TokenClaim(Constant.TOKEN_KEY_CLAIM, user.nim))

            call.buildSuccessJson { TokenResponse(token) }
        }
    }

    private fun Route.signIn() {
        post("/login") {
            val body = call.receive<UserLoginRequest>()
            val user = repository.getUserByIdentityNumber(body.nim)

            if (user == null) {
                call.buildErrorJson(message = "user not found")
                return@post
            }

            middleware.apply {
                call.verifyPassword(
                    body.password, SaltedHash(
                        hash = user.password,
                        salt = user.salt
                    )
                )
            }

            val token = middleware.generateToken(TokenClaim(Constant.TOKEN_KEY_CLAIM, user.nim))
            call.buildSuccessJson { TokenResponse(token) }
        }
    }

    private fun Route.signOut() {
        post("/logout") {
            val jwt = call.request.header("Authorization")?.substring("Bearer ".length)
            middleware.apply { application.invalidateToken(jwt ?: "") }
            call.buildSuccessJson { "Sign out success" }
        }
    }

    fun Route.initRoutes() {
        signUp()
        signIn()
        signOut()
    }
}