package com.college.data.repository.consultation

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.college.data.database.DatabaseFactory
import com.college.data.table.ConsultationTable
import com.college.data.table.ConsultationTimeTable
import com.college.model.request.consultation.ConsultationRequest
import com.college.model.response.activity.ActivityResponse
import com.college.model.response.consultation.AddConsultationResponse
import com.college.model.response.consultation.ConsultationDetailResponse
import com.college.model.response.consultation.ConsultationListResponse
import com.college.utils.*
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class ConsultationRepositoryImpl(private val dbFactory: DatabaseFactory) : ConsultationRepository {
    override suspend fun insertConsultation(nim: String, body: ConsultationRequest): AddConsultationResponse {
        val timestamp = createTimeStamp(DateFormat.DATE_TIME)
        val idCreated = "CONSULTATION-${NanoIdUtils.randomNanoId()}"
        dbFactory.dbQuery {
            ConsultationTable.insert { table ->
                table[consultationId] = idCreated
                table[uid] = nim
                table[story] = body.story
                table[counselorChoice] = body.counselorChoice
                table[consultationType] = body.consultationType
                table[progressIndex] = Progress.PROCESS.index
                table[date] = body.date
                table[time] = body.time
                table[postDate] = timestamp
                table[updateDate] = timestamp
            }
        }
        return AddConsultationResponse(idCreated)
    }

    override suspend fun getConsultationByNim(nim: String): List<ActivityResponse> = dbFactory.dbQuery {
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
                ConsultationTable.progressIndex,
                ConsultationTable.updateDate,
                ConsultationTimeTable.startTime,
                ConsultationTimeTable.endTime
            )
            .select { ConsultationTable.uid.eq(nim) }.mapNotNull { it.toConsultationList() }
    }

    override suspend fun getInProgressConsultation(nim: String): List<ActivityResponse> = dbFactory.dbQuery {
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
                ConsultationTable.progressIndex,
                ConsultationTable.updateDate,
                ConsultationTimeTable.startTime,
                ConsultationTimeTable.endTime
            )
            .select { ConsultationTable.uid.eq(nim).and(ConsultationTable.progressIndex.eq(Progress.PROCESS.index)) }.mapNotNull { it.toConsultationList() }
    }

    override suspend fun getHistoryConsultation(nim: String): List<ActivityResponse> = dbFactory.dbQuery {
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
                ConsultationTable.progressIndex,
                ConsultationTable.updateDate,
                ConsultationTimeTable.startTime,
                ConsultationTimeTable.endTime
            )
            .select { ConsultationTable.uid.eq(nim).and(ConsultationTable.progressIndex.eq(Progress.DONE.index)) }.mapNotNull { it.toConsultationList() }
    }

    override suspend fun getConsultationDetail(nim: String, consultationId: String): ConsultationDetailResponse =
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
                    ConsultationTable.progressIndex,
                    ConsultationTable.postDate,
                    ConsultationTable.updateDate,
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