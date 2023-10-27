package com.college.data.table

import org.jetbrains.exposed.sql.Table

object ReportTable : Table() {
    override val tableName: String
        get() = "report"
    val reportId = varchar("report_id", 128)
    val uid = reference("user_id", UserTable.userId)
    val story = varchar("story", 512)
    val isNeedConsultation = bool("is_need_consultation")
    val progressIndex = integer("progress_index")
    val postDate = varchar("post_date", 64).nullable()
    val updateDate = varchar("update_date", 64)
    val consultationId = reference("consultation_id", ConsultationTable.consultationId).nullable()

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(reportId)
}