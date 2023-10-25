package com.college.data.repository.consultation

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.college.data.database.DatabaseFactory
import com.college.data.table.ConsultationTable
import com.college.data.table.ConsultationTimeTable
import com.college.data.table.UserTable
import com.college.model.request.consultation.ConsultationRequest
import com.college.model.response.consultation.ConsultationResponse
import com.college.utils.toConsultation
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class ConsultationRepositoryImpl(private val dbFactory: DatabaseFactory) : ConsultationRepository {
    override suspend fun insertConsultation(nim: String, body: ConsultationRequest): String {
        val idCreated = "CONSULTATION-${NanoIdUtils.randomNanoId()}"
        dbFactory.dbQuery {
            ConsultationTable.insert { table ->
                table[consultationId] = idCreated
                table[uid] = nim
                table[story] = body.story
                table[counselorChoice] = body.counselorChoice
                table[consultationType] = body.consultationType
                table[date] = body.date
                table[time] = body.time
            }
        }
        return idCreated
    }

    override suspend fun getConsultationByNim(nim: String): List<ConsultationResponse> = dbFactory.dbQuery {
        ConsultationTable
            .join(
                ConsultationTimeTable,
                JoinType.INNER
            ) { ConsultationTable.time.eq(ConsultationTimeTable.consultationTimeId) }
            .slice(
                ConsultationTable.consultationId,
                ConsultationTable.uid,
                ConsultationTable.story,
                ConsultationTable.counselorChoice,
                ConsultationTable.consultationType,
                ConsultationTable.date,
                ConsultationTable.time,
                ConsultationTimeTable.startTime,
                ConsultationTimeTable.endTime
            )
            .select { ConsultationTable.uid.eq(nim) }.mapNotNull { it.toConsultation() }
    }

    override suspend fun getConsultationDetail(nim: String, consultationId: String): ConsultationResponse =
        dbFactory.dbQuery {
            ConsultationTable
                .join(
                    ConsultationTimeTable,
                    JoinType.INNER
                ) { ConsultationTable.time.eq(ConsultationTimeTable.consultationTimeId) }
                .slice(
                    ConsultationTable.consultationId,
                    ConsultationTable.uid,
                    ConsultationTable.story,
                    ConsultationTable.counselorChoice,
                    ConsultationTable.consultationType,
                    ConsultationTable.date,
                    ConsultationTable.time,
                    ConsultationTimeTable.startTime,
                    ConsultationTimeTable.endTime
                )
                .select { ConsultationTable.consultationId.eq(consultationId).and { ConsultationTable.uid.eq(nim) } }
                .groupBy(ConsultationTable.consultationId)
                .groupBy(ConsultationTimeTable.consultationTimeId)
                .firstNotNullOf {
                    it.toConsultation()
                }
        }
}