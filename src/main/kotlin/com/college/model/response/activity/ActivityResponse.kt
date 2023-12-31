package com.college.model.response.activity

import kotlinx.serialization.Serializable

@Serializable
data class ActivityResponse(
    val activityId: String,
    val title: String,
    val updateDate: String,
    val progress: String,
)
