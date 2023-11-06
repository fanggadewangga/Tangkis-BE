package com.college.route

import com.college.data.repository.consultation.ConsultationRepository
import com.college.data.repository.report.ReportRepository
import com.college.model.response.activity.ActivityResponse
import com.college.route.RouteResponseHelper.buildErrorJson
import com.college.route.RouteResponseHelper.buildSuccessListJson
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

class ActivityRoute(private val reportRepository: ReportRepository, private val consultationRepository: ConsultationRepository) {
    private fun Route.getInProgressActivity() {
        authenticate {
            get("/user/{nim}/activity/progress") {
                val nim = call.parameters["nim"] ?: ""
                val inProgressActivity = mutableListOf<ActivityResponse>()
                try {
                    val inProgressReport = reportRepository.getInProgressReport(nim)
                    val inProgressConsultation = consultationRepository.getInProgressConsultation(nim)
                    inProgressActivity.apply {
                        addAll(inProgressReport)
                        addAll(inProgressConsultation)
                    }
                    call.buildSuccessListJson { inProgressActivity }
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                }
            }
        }
    }

    private fun Route.getHistoryActivity() {
        authenticate {
            get("/user/{nim}/activity/history") {
                val nim = call.parameters["nim"] ?: ""
                val historyActivity = mutableListOf<ActivityResponse>()
                try {
                    val historyReport = reportRepository.getHistoryReport(nim)
                    val historyConsultation = consultationRepository.getHistoryConsultation(nim)
                    historyActivity.apply {
                        addAll(historyReport)
                        addAll(historyConsultation)
                    }
                    call.buildSuccessListJson { historyActivity }
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                }
            }
        }
    }

    fun Route.initRoute() {
        getInProgressActivity()
        getHistoryActivity()
    }
}