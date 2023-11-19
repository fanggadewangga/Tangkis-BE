package com.college.data.table

import org.jetbrains.exposed.sql.Table

object ArticleTable: Table() {
    override val tableName: String
        get() = "article"

    val articleId = varchar("article_id", 128)
    val title = varchar("title", 512)
    val content = varchar("content", 3072)
    val imageUrl = varchar("image_url", 2048)
    val postDate = varchar("post_date", 64)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(articleId)
}