package com.college.route

import com.college.data.repository.consultation_time.ConsultationTimeRepository
import com.college.route.RouteResponseHelper.buildSuccessJson
import io.ktor.server.application.*
import io.ktor.server.routing.*

class ConsultationTimeRoute(private val repository: ConsultationTimeRepository) {
    private fun Route.postTime() {
        post("/consultation/time") {
            repository.insertConsultationTime()
            call.buildSuccessJson{"Sukses prepopulated"}
        }
    }

    fun Route.initRoutes() {
        postTime()
    }
}