package com.college.plugins

import com.college.route.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val authRoute by inject<AuthRoute>()
    val contactRoute by inject<ContactRoute>()
    val reportRoute by inject<ReportRoute>()
    val consultationRoute by inject<ConsultationRoute>()
    val consultationTimeRoute by inject<ConsultationTimeRoute>()
    val userRoute by inject<UserRoute>()
    val articleRoute by inject<ArticleRoute>()

    routing {
        get("/") {
            call.respondText("Tangkis Backend Base URL")
        }
        authRoute.apply { this@routing.initRoutes() }
        contactRoute.apply { initRoute() }
        reportRoute.apply { initRoutes() }
        consultationRoute.apply { initRoutes() }
        consultationTimeRoute.apply { initRoutes() }
        userRoute.apply { initRoute() }
        articleRoute.apply { initRoute() }
    }
}
