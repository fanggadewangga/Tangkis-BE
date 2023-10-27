package com.college.data.repository.consultation

import com.college.model.request.consultation.ConsultationRequest
import com.college.model.response.consultation.ConsultationDetailResponse
import com.college.model.response.consultation.ConsultationListResponse

interface ConsultationRepository {
    suspend fun insertConsultation(nim: String, body: ConsultationRequest): String // clear
    suspend fun getConsultationByNim(nim: String): List<ConsultationListResponse> // clear
    suspend fun getConsultationDetail(nim: String, consultationId: String): ConsultationDetailResponse // clear
}