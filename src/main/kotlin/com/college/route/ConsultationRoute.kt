package com.college.route

import com.college.data.repository.consultation.ConsultationRepository
import com.college.model.request.consultation.ConsultationRequest
import com.college.route.RouteResponseHelper.buildErrorJson
import com.college.route.RouteResponseHelper.buildSuccessJson
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

class ConsultationRoute(
    private val consultationRepository: ConsultationRepository
) {
    private fun Route.postConsultation() {
        authenticate {
            post("/user/{nim}/consultation") {
                val nim = call.parameters["nim"] ?: ""
                val body = try {
                    call.receive<ConsultationRequest>()
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                    return@post
                }

                try {
                    val addConsultationResponse =  consultationRepository.insertConsultation(nim = nim, body = body)
                    call.buildSuccessJson { addConsultationResponse }
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                }
            }
        }
    }

    private fun Route.getConsultations() {
        authenticate {
            get("/user/{nim}/consultation") {
                val nim = call.parameters["nim"] ?: ""
                try {
                    val consultations = consultationRepository.getConsultationByNim(nim)
                    call.buildSuccessJson { consultations }
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                }
            }
        }
    }

    private fun Route.getConsultationDetail() {
        authenticate {
            get("/user/{nim}/consultation/{consultationId}") {
                val nim = call.parameters["nim"] ?: ""
                val consultationId = call.parameters["consultationId"] ?: ""
                try {
                    val consultationDetail = consultationRepository.getConsultationDetail(nim, consultationId)
                    call.buildSuccessJson { consultationDetail }
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                }
            }
        }
    }

    fun Route.initRoutes() {
        postConsultation()
        getConsultations()
        getConsultationDetail()
    }
}