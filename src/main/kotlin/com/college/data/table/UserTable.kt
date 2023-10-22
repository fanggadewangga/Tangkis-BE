package com.college.data.table

import org.jetbrains.exposed.sql.Table

object UserTable : Table() {
    override val tableName: String
        get() = "user"

    val userId = varchar("user_id", 512)
    val password = varchar("password", 1024)
    val salt = varchar("salt", 1024)
    val name = varchar("name", 128)
    val whatsapp = varchar("whatsapp", 32)
    val studyProgram = varchar("study_program", 64).nullable()

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(userId)
}