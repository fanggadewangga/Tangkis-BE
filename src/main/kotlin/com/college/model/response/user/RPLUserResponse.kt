package com.college.model.response.user

import kotlinx.serialization.Serializable

@Serializable
data class RPLUserResponse(
    val nim: String,
    val name: String,
    val password: String,
)
