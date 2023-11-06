package com.college.data.repository.consultation

import com.college.model.request.consultation.ConsultationRequest
import com.college.model.response.activity.ActivityResponse
import com.college.model.response.consultation.AddConsultationResponse
import com.college.model.response.consultation.ConsultationDetailResponse
import com.college.model.response.consultation.ConsultationListResponse

interface ConsultationRepository {
    suspend fun insertConsultation(nim: String, body: ConsultationRequest): AddConsultationResponse // clear
    suspend fun getConsultationByNim(nim: String): List<ActivityResponse> // clear
    suspend fun getInProgressConsultation(nim: String): List<ActivityResponse> // not clear
    suspend fun getHistoryConsultation(nim: String): List<ActivityResponse> // not clear
    suspend fun getConsultationDetail(nim: String, consultationId: String): ConsultationDetailResponse // clear
}