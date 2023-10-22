package com.college.model.request.consultation

import kotlinx.serialization.Serializable

@Serializable
data class ConsultationRequest(
    val story: String,
    val counselorChoice: Int = 0,
    val consultationType: Int,
    val date: String,
    val time: String
)
