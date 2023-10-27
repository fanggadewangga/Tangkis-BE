package com.college.data.repository.article

import com.college.model.request.article.ArticleRequest
import com.college.model.response.article.ArticleListResponse
import com.college.model.response.article.ArticleResponse

interface ArticleRepository {
    suspend fun insertArticle(body: ArticleRequest) // not clear
    suspend fun getArticles(): List<ArticleListResponse> // not clear
    suspend fun searchArticle(query: String): List<ArticleListResponse> // not clear
    suspend fun getArticleDetail(articleId: String): ArticleResponse // not clear
}