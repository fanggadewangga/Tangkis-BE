package com.college.route

import com.college.model.response.BaseListResponse
import com.college.model.response.BaseResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*

object RouteResponseHelper {

    suspend inline fun <reified T> ApplicationCall.buildSuccessJson(
        messagePlaceholder: String? = null,
        noinline action: suspend () -> T
    ) {
        try {
            val data = action()
            this.respond(
                HttpStatusCode.OK,
                BaseResponse(
                    error = false,
                    status = HttpStatusCode.OK.value.toString(),
                    message = "Success",
                    data = if (data is Unit) messagePlaceholder else data
                )
            )
        } catch (e: Exception) {
            this@buildSuccessJson.buildErrorJson(e)
        }
    }

    suspend inline fun ApplicationCall.buildErrorJson(
        httpStatusCode: HttpStatusCode = HttpStatusCode.Conflict,
        message: String
    ) {
        this.respond(
            httpStatusCode,
            BaseResponse(
                error = true,
                status = httpStatusCode.value.toString(),
                message = message,
                data = null
            )
        )
    }

    suspend inline fun ApplicationCall.buildErrorJson(exception: Exception) {
        val baseResponse = BaseResponse(
            error = true,
            status = HttpStatusCode.BadRequest.value.toString(),
            message = exception.message.toString(),
            data = null
        )
        when (exception) {
            is BadRequestException -> this.respond(
                HttpStatusCode.BadRequest,
                baseResponse
            )

            is NotFoundException -> {
                baseResponse.status = HttpStatusCode.NotFound.value.toString()
                this.respond(
                    HttpStatusCode.NotFound,
                    baseResponse
                )
            }

            else -> {
                baseResponse.status = HttpStatusCode.Conflict.value.toString()
                this.respond(
                    HttpStatusCode.Conflict,
                    baseResponse
                )
            }
        }
    }

    suspend inline fun <reified T> ApplicationCall.buildSuccessListJson(noinline action: suspend () -> T) {
        try {
            val count = count { action() as List<*> }
            this.respond(
                HttpStatusCode.OK,
                BaseListResponse(
                    error = false,
                    HttpStatusCode.OK.value.toString(),
                    "Request Success",
                    count,
                    action()
                )
            )

        } catch (e: Exception) {
            this@buildSuccessListJson.buildErrorListJson(e)
        }
    }

    suspend inline fun ApplicationCall.buildErrorListJson(e: Exception) {
        val listResponse =
            BaseListResponse(error = true, message = e.message.toString(), count = 0, data = arrayListOf<Any>())
        when (e) {
            is BadRequestException -> {
                listResponse.status = HttpStatusCode.BadRequest.value.toString()
                this.respond(
                    HttpStatusCode.OK,
                    listResponse
                )
            }

            is NotFoundException -> {
                listResponse.status = HttpStatusCode.NotFound.value.toString()
                this.respond(
                    HttpStatusCode.OK,
                    listResponse
                )
            }

            else -> {
                listResponse.status = HttpStatusCode.Conflict.value.toString()
                this.respond(
                    HttpStatusCode.OK,
                    listResponse
                )
            }
        }
    }

    inline fun count(block: () -> List<*>) = block().size
}