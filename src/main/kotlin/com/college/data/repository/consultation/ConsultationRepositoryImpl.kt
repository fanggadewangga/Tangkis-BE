package com.college.data.repository.consultation

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.college.data.database.DatabaseFactory
import com.college.data.table.ConsultationTable
import com.college.model.request.consultation.ConsultationRequest
import com.college.model.response.consultation.ConsultationResponse
import com.college.utils.toConsultation
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
        ConsultationTable.select { ConsultationTable.uid.eq(nim) }.mapNotNull { it.toConsultation() }
    }
}