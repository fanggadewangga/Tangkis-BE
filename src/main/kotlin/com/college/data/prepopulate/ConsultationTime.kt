package com.college.data.prepopulate

import com.college.model.request.consultation_time.ConsultationTime

object ConsultationTime {
    fun getInitialConsultationTime() = listOf(
        ConsultationTime(
            consultationTimeId = "TIME-1",
            startTime = "13.00",
            endTime = "14.00"
        ),
        ConsultationTime(
            consultationTimeId = "TIME-2",
            startTime = "14.00",
            endTime = "15.00"
        ),
        ConsultationTime(
            consultationTimeId = "TIME-3",
            startTime = "15.00",
            endTime = "16.00"
        ),
    )
}