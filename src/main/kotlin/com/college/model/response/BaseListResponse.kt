package com.college.model.response

import kotlinx.serialization.Serializable

@Serializable
data class BaseListResponse<T>(
    val error: Boolean,
    var status: String = "",
    val message: String = "",
    val count: Int = 0,
    val data: T?
)