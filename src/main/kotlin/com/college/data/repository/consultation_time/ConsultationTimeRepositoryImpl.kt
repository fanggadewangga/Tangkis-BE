package com.college.data.repository.consultation_time

import com.college.data.database.DatabaseFactory
import com.college.data.prepopulate.ConsultationTime
import com.college.data.table.ConsultationTimeTable
import org.jetbrains.exposed.sql.insert

class ConsultationTimeRepositoryImpl(private val dbFactory: DatabaseFactory) : ConsultationTimeRepository {
    override suspend fun insertConsultationTime() {
        dbFactory.dbQuery {
            ConsultationTime.getInitialConsultationTime().forEach { time ->
                ConsultationTimeTable.insert { table ->
                    table[this.consultationTimeId] = time.consultationTimeId
                    table[startTime] = time.startTime
                    table[endTime] = time.endTime
                }
            }
        }
    }
}