package com.college.data.repository.report

import com.college.model.request.report.ReportRequest
import com.college.model.response.report.ReportResponse

interface ReportRepository {
    suspend fun insertReport(nim: String, body: ReportRequest, consultationId: String? = null) // clear
    suspend fun getReportByNim(nim: String): List<ReportResponse> // report
    suspend fun updateReportProgress(reportId: String) // clear
}