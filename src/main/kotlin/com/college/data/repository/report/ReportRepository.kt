package com.college.data.repository.report

import com.college.model.request.report.ReportRequest
import com.college.model.response.activity.ActivityResponse
import com.college.model.response.report.AddReportResponse
import com.college.model.response.report.ReportDetailResponse
import com.college.model.response.report.ReportResponse

interface ReportRepository {
    suspend fun insertReport(nim: String, body: ReportRequest, consultationId: String? = null) : AddReportResponse// clear
    suspend fun getReportByNim(nim: String): List<ActivityResponse> // clear
    suspend fun getInProgressReport(nim: String): List<ActivityResponse> // not clear
    suspend fun getHistoryReport(nim: String): List<ActivityResponse> // not clear
    suspend fun getReportDetail(nim: String, reportId: String): ReportDetailResponse // clear
    suspend fun updateReportProgress(reportId: String) // clear
}