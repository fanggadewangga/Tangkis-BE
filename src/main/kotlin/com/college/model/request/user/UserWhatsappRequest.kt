package com.college.model.request.user

import kotlinx.serialization.Serializable

@Serializable
data class UserWhatsappRequest(
    val whatsapp: String
)
