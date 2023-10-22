package com.college.model.response.report

import kotlinx.serialization.Serializable

@Serializable
data class ReportResponse(
    val reportId: String,
    val nim: String,
    val story: String,
    val isNeedConsultation: Boolean,
    val consultationId: String? = "",
    val progress: String,
    val date: String
)
