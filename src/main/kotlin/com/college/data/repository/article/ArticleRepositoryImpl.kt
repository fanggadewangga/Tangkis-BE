package com.college.data.repository.article

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.college.data.database.DatabaseFactory
import com.college.data.table.ArticleTable
import com.college.model.request.article.ArticleRequest
import com.college.model.response.article.ArticleListResponse
import com.college.model.response.article.ArticleResponse
import com.college.utils.DateFormat
import com.college.utils.createTimeStamp
import com.college.utils.toArticleDetail
import com.college.utils.toArticles
import org.jetbrains.exposed.sql.LowerCase
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.util.*

class ArticleRepositoryImpl(private val dbFactory: DatabaseFactory) : ArticleRepository {
    override suspend fun insertArticle(body: ArticleRequest) {
        val timestamp = createTimeStamp(DateFormat.DATE_TIME)
        val idCreated = "ARTICLE-${NanoIdUtils.randomNanoId()}"
        dbFactory.dbQuery {
            ArticleTable.insert { table ->
                table[articleId] = idCreated
                table[title] = body.title
                table[content] = body.content
                table[imageUrl] = body.imageUrl
                table[postDate] = timestamp
            }
        }
    }

    override suspend fun getArticles(): List<ArticleListResponse> = dbFactory.dbQuery {
        ArticleTable.selectAll().map { it.toArticles() }
    }

    override suspend fun searchArticle(query: String): List<ArticleListResponse> =
        dbFactory.dbQuery {
            ArticleTable.select {
                LowerCase(ArticleTable.title).like("%$query%".lowercase(Locale.getDefault()))
            }.groupBy(ArticleTable.articleId)
                .mapNotNull {
                    it.toArticles()
                }
        }

    override suspend fun getArticleDetail(articleId: String): ArticleResponse = dbFactory.dbQuery {
        ArticleTable.select {
            ArticleTable.articleId.eq(articleId)
        }.firstNotNullOf {
            it.toArticleDetail()
        }
    }
}