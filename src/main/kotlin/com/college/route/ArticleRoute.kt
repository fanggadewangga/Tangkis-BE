package com.college.route

import com.college.data.repository.article.ArticleRepository
import com.college.model.request.article.ArticleRequest
import com.college.route.RouteResponseHelper.buildErrorJson
import com.college.route.RouteResponseHelper.buildSuccessJson
import com.college.route.RouteResponseHelper.buildSuccessListJson
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

class ArticleRoute(private val articleRepository: ArticleRepository) {
    private fun Route.postArticle() {
        authenticate {
            post("/article") {
                val body = try {
                    call.receive<ArticleRequest>()
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                    return@post
                }

                try {
                    articleRepository.insertArticle(body)
                    call.buildSuccessJson { "Sukses menambahkan artikel" }
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                }
            }
        }
    }

    private fun Route.getArticles() {
        authenticate {
            get("/article") {
                val query = try {
                    call.request.queryParameters["q"]
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                    return@get
                }

                if (query != null)
                    call.buildSuccessListJson { articleRepository.searchArticle(query) }
                else
                    call.buildSuccessListJson { articleRepository.getArticles() }
            }
        }
    }

    private fun Route.getArticleDetail() {
        authenticate {
            get("/article/{articleId}") {
                val articleId = try {
                    call.parameters["articleId"]
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                    return@get
                }
                call.buildSuccessJson { articleRepository.getArticleDetail(articleId!!) }
            }
        }
    }

    fun Route.initRoute() {
        postArticle()
        getArticles()
        getArticleDetail()
    }
}