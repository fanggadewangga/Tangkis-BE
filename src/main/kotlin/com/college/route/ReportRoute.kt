package com.college.route

import com.college.data.repository.consultation.ConsultationRepository
import com.college.data.repository.report.ReportRepository
import com.college.model.request.report.ReportRequest
import com.college.route.RouteResponseHelper.buildErrorJson
import com.college.route.RouteResponseHelper.buildSuccessJson
import com.college.route.RouteResponseHelper.buildSuccessListJson
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlin.Exception

class ReportRoute(
    private val reportRepository: ReportRepository,
    private val consultationRepository: ConsultationRepository
) {
    private fun Route.postReport() {
        authenticate {
            post("/user/{nim}/report") {

                val nim = call.parameters["nim"] ?: ""
                val body = try {
                    call.receive<ReportRequest>()
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                    return@post
                }

                try {
                    if (!body.isNeedConsultation && body.consultation == null) {
                        reportRepository.insertReport(nim = nim, body = body)
                    } else {
                        val uid = consultationRepository.insertConsultation(nim = nim, body = body.consultation!!)
                        reportRepository.insertReport(nim = nim, body = body, consultationId = uid)
                    }
                    call.buildSuccessJson { "Sukses mengirim laporan" }
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                }
            }
        }
    }

    private fun Route.getReport() {
        authenticate {
            get("/user/{nim}/report") {
                val uid = call.parameters["nim"] ?: ""
                try {
                    val reports = reportRepository.getReportByNim(uid)
                    call.buildSuccessListJson { reports }
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                }
            }
        }
    }

    private fun Route.getReportDetail() {
        authenticate {
            get("/user/{nim}/report/{reportId}") {
                val uid = call.parameters["nim"] ?: ""
                val reportId = call.parameters["reportId"] ?: ""
                try {
                    val report = reportRepository.getReportDetail(nim = uid, reportId = reportId)
                    call.buildSuccessJson { report }
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                }
            }
        }
    }

    private fun Route.updateReportProgress() {
        authenticate {
            put("/report/{reportId}") {
                val reportId = call.parameters["reportId"] ?: ""
                try {
                    reportRepository.updateReportProgress(reportId)
                    call.buildSuccessJson { "Sukses mengupdate progress laporan" }
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                }
            }
        }
    }

    fun Route.initRoutes() {
        postReport()
        getReport()
        getReportDetail()
        updateReportProgress()
    }
}