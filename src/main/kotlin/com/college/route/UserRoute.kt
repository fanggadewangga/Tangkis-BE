package com.college.route

import com.college.data.repository.user.UserRepository
import com.college.middleware.Middleware
import com.college.model.request.user.UserPasswordRequest
import com.college.model.request.user.UserWhatsappRequest
import com.college.route.RouteResponseHelper.buildErrorJson
import com.college.route.RouteResponseHelper.buildSuccessJson
import com.college.security.hashing.SaltedHash
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

class UserRoute(
    private val userRepository: UserRepository,
    private val middleware: Middleware
) {
    private fun Route.getUserDetail() {
        authenticate {
            get("/user/{nim}") {
                val nim = call.parameters["nim"] ?: ""
                try {
                    val user = userRepository.getUserDetail(nim)
                    call.buildSuccessJson { user }
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                }
            }
        }
    }

    private fun Route.getRPLUserDetail() {
        authenticate {
            get("/rpl/user/{nim}") {
                val nim = call.parameters["nim"] ?: ""
                try {
                    val user = userRepository.getRPLUserDetail(nim)
                    call.buildSuccessJson { user }
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                }
            }
        }
    }

    private fun Route.updateUserWhatsapp() {
        authenticate {
            put("/user/{nim}/whatsapp") {
                val nim = call.parameters["nim"] ?: ""
                val body = try {
                    call.receive<UserWhatsappRequest>()
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                    return@put
                }

                try {
                    userRepository.updateUserWhatsapp(nim, body.whatsapp)
                    call.buildSuccessJson { "Sukses mengubah nomor Whatsapp" }
                } catch (e: Exception) {
                    call.buildErrorJson(message = "Gagal mengubah nomor Whatsapp")
                }
            }
        }
    }

    private fun Route.updateUserPassword() {
        authenticate {
            put("/user/{nim}/password") {
                val nim = call.parameters["nim"] ?: ""
                val jwt = call.request.header("Authorization")?.substring("Bearer ".length)
                val body = try {
                    call.receive<UserPasswordRequest>()
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                    return@put
                }
                val isPasswordTooSort = body.newPassword.length < 8

                val user = userRepository.getUserByIdentityNumber(nim)

                if (user == null) {
                    call.buildErrorJson(message = "user not found")
                    return@put
                }

                middleware.apply {
                    call.verifyPassword(
                        body.oldPassword, SaltedHash(
                            hash = user.password,
                            salt = user.salt
                        )
                    )
                }

                if (isPasswordTooSort) {
                    call.buildErrorJson(message = "Password harus lebih dari 8 karakter")
                } else {
                    val saltedHash = middleware.hashPassword(body.newPassword)
                    userRepository.updateUserPassword(nim, saltedHash)
                    middleware.apply { application.invalidateToken(jwt ?: "") }
                    call.buildSuccessJson { "Sukses mengubah password" }
                }
            }
        }
    }

    fun Route.initRoute() {
        getUserDetail()
        getRPLUserDetail()
        updateUserWhatsapp()
        updateUserPassword()
    }
}

