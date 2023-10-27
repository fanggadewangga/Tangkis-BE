package com.college.model.request.article

import kotlinx.serialization.Serializable

@Serializable
data class ArticleRequest(
    val title: String,
    val content: String,
    val imageUrl: String
)
