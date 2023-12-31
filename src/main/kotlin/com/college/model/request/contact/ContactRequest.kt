package com.college.model.request.contact

import kotlinx.serialization.Serializable

@Serializable
data class ContactRequest(
    val name: String,
    val number: String
)
