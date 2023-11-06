package com.college.data.repository.report

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.college.data.database.DatabaseFactory
import com.college.data.table.ReportTable
import com.college.data.table.UserTable
import com.college.model.request.report.ReportRequest
import com.college.model.response.activity.ActivityResponse
import com.college.model.response.report.AddReportResponse
import com.college.model.response.report.ReportDetailResponse
import com.college.model.response.report.ReportResponse
import com.college.utils.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ReportRepositoryImpl(private val dbFactory: DatabaseFactory) : ReportRepository {
    override suspend fun insertReport(nim: String, body: ReportRequest, consultationId: String?): AddReportResponse {
        val idCreated = "REPORT-${NanoIdUtils.randomNanoId()}"
        dbFactory.dbQuery {
            val date = createTimeStamp(DateFormat.DATE_TIME)
            ReportTable.insert { table ->
                table[reportId] = idCreated
                table[uid] = nim
                table[story] = body.story
                table[isNeedConsultation] = body.isNeedConsultation
                table[progressIndex] = Progress.PROCESS.index
                table[postDate] = date
                table[updateDate] = date
                table[this.consultationId] = consultationId
            }
        }
        return AddReportResponse(idCreated)
    }

    override suspend fun getReportByNim(nim: String): List<ActivityResponse> = dbFactory.dbQuery {
        ReportTable.select { ReportTable.uid.eq(nim) }.mapNotNull { it.toReports() }
    }

    override suspend fun getInProgressReport(nim: String): List<ActivityResponse> = dbFactory.dbQuery {
        ReportTable.select { ReportTable.uid.eq(nim).and(ReportTable.progressIndex.eq(Progress.PROCESS.index)) }.mapNotNull { it.toReports() }
    }

    override suspend fun getHistoryReport(nim: String): List<ActivityResponse> = dbFactory.dbQuery {
        ReportTable.select { ReportTable.uid.eq(nim).and(ReportTable.progressIndex.eq(Progress.DONE.index)) }.mapNotNull { it.toReports() }
    }

    override suspend fun getReportDetail(nim: String, reportId: String): ReportDetailResponse = dbFactory.dbQuery {
        ReportTable
            .join(UserTable, JoinType.INNER) { ReportTable.uid.eq(UserTable.userId) }
            .slice(
                UserTable.userId,
                UserTable.name,
                UserTable.whatsapp,
                ReportTable.reportId,
                ReportTable.uid,
                ReportTable.story,
                ReportTable.isNeedConsultation,
                ReportTable.progressIndex,
                ReportTable.postDate,
                ReportTable.updateDate,
                ReportTable.consultationId
            )
            .select(ReportTable.reportId.eq(reportId))
            .groupBy(ReportTable.reportId)
            .groupBy(UserTable.userId)
            .firstNotNullOf {
                it.toReportDetail()
            }
    }

    override suspend fun updateReportProgress(reportId: String) {
        dbFactory.dbQuery {
            val date = createTimeStamp(DateFormat.DATE_TIME)
            val currentIndex = ReportTable.select { ReportTable.reportId.eq(reportId) }
                .firstNotNullOf { it[ReportTable.progressIndex] }

            ReportTable.update({ ReportTable.reportId.eq(reportId) }) {
                it[updateDate] = date
                it[progressIndex] = currentIndex.plus(1)
            }
        }
    }
}