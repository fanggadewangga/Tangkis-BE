package com.college.data.repository.consultation

import com.college.model.request.consultation.ConsultationRequest
import com.college.model.response.consultation.ConsultationResponse

interface ConsultationRepository {
    suspend fun insertConsultation(nim: String, body: ConsultationRequest): String // clear
    suspend fun getConsultationByNim(nim: String): List<ConsultationResponse> // clear
}