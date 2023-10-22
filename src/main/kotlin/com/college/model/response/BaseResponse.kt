package com.college.model.response

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val error: Boolean,
    var status: String = "",
    var message: String = "",
    val data: T?
)