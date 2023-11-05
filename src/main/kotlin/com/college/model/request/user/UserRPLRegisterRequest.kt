package com.college.model.request.user

import kotlinx.serialization.Serializable

@Serializable
data class UserRPLRegisterRequest(
    val name: String,
    val nim: String,
    val password: String
)
