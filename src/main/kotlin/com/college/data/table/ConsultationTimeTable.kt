package com.college.data.table

import org.jetbrains.exposed.sql.Table

object ConsultationTimeTable: Table() {
    override val tableName: String
        get() = "consultation_time"

    val consultationTimeId = varchar("consultation_time_id", 128)
    val startTime = varchar("start_time", 32)
    val endTime = varchar("end_time", 32)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(consultationTimeId)
}