package com.college.model.request.user

data class User(
    val nim: String,
    val name: String,
    val whatsapp: String,
    val studyProgram: String? = "",
    val password: String,
    val salt: String
)