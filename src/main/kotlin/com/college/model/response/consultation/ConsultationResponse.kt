package com.college.model.response.consultation

import kotlinx.serialization.Serializable

@Serializable
data class ConsultationResponse(
    val consultationId: String,
    val uid: String,
    val story: String,
    val counselorChoice: String,
    val consultationType: String,
    val date: String,
    val time: String
)
