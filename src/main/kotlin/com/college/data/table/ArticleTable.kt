package com.college.data.table

import org.jetbrains.exposed.sql.Table

object ArticleTable: Table() {
    override val tableName: String
        get() = "article"

    val articleId = varchar("article_id", 128)
    val title = varchar("title", 128)
    val content = varchar("content", 1024)
    val imageUrl = varchar("image_url", 1024)
    val postDate = varchar("post_date", 64)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(articleId)
}