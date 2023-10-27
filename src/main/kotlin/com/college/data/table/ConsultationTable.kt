package com.college.data.table

import org.jetbrains.exposed.sql.Table

object ConsultationTable : Table() {
    override val tableName: String
        get() = "consultation"

    val consultationId = varchar("consultation_id", 128)
    val uid = reference("user_id", UserTable.userId)
    val story = varchar("story", 512)
    val counselorChoice = integer("counselor_choice")
    val consultationType = integer("consultation_type")
    val date = varchar("date", 128)
    val time = reference("consultation_time_id", ConsultationTimeTable.consultationTimeId)
    val progressIndex = integer("progress_index")
    val postDate = varchar("post_date", 64)
    val updateDate = varchar("update_date", 64)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(consultationId)
}