package com.college.model.response.consultation

import kotlinx.serialization.Serializable

@Serializable
data class ConsultationListResponse(
    val consultationId: String,
    val title: String,
    val progress: String,
    val updatedAt: String,
)
