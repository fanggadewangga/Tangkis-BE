package com.college.model.response.token

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String
)