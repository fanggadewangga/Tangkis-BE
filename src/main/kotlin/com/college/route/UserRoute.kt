package com.college.route

import com.college.data.repository.user.UserRepository
import com.college.middleware.Middleware
import com.college.model.request.user.UserPasswordRequest
import com.college.model.request.user.UserWhatsappRequest
import com.college.route.RouteResponseHelper.buildErrorJson
import com.college.route.RouteResponseHelper.buildSuccessJson
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
                val isPasswordTooSort = body.password.length < 8

                if (isPasswordTooSort) {
                    call.buildErrorJson(message = "Password harus lebih dari 8 karakter")
                } else {
                    val saltedHash = middleware.hashPassword(body.password)
                    userRepository.updateUserPassword(nim, saltedHash)
                    middleware.apply { application.invalidateToken(jwt ?: "") }
                    call.buildSuccessJson { "Sukses mengubah password" }
                }
            }
        }
    }

    fun Route.initRoute() {
        getUserDetail()
        updateUserWhatsapp()
        updateUserPassword()
    }
}

