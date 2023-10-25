package com.college.data.repository.report

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.college.data.database.DatabaseFactory
import com.college.data.table.ReportTable
import com.college.data.table.UserTable
import com.college.model.request.report.ReportRequest
import com.college.model.response.report.ReportDetailResponse
import com.college.model.response.report.ReportResponse
import com.college.utils.*
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class ReportRepositoryImpl(private val dbFactory: DatabaseFactory) : ReportRepository {
    override suspend fun insertReport(nim: String, body: ReportRequest, consultationId: String?) {
        dbFactory.dbQuery {
            val date = createTimeStamp(DateFormat.DATE_TIME)
            val idCreated = "REPORT-${NanoIdUtils.randomNanoId()}"
            ReportTable.insert { table ->
                table[reportId] = idCreated
                table[uid] = nim
                table[story] = body.story
                table[isNeedConsultation] = body.isNeedConsultation
                table[progressIndex] = ReportProgress.PROCESS.index
                table[postDate] = date
                table[updateDate] = date
                table[this.consultationId] = consultationId
            }
        }
    }

    override suspend fun getReportByNim(nim: String): List<ReportResponse> = dbFactory.dbQuery {
        ReportTable.select { ReportTable.uid.eq(nim) }.mapNotNull { it.toReports() }
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
            val date = createTimeStamp(DateFormat.DATE)
            val currentIndex = ReportTable.select { ReportTable.reportId.eq(reportId) }
                .firstNotNullOf { it[ReportTable.progressIndex] }

            ReportTable.update({ ReportTable.reportId.eq(reportId) }) {
                it[updateDate] = date
                it[progressIndex] = currentIndex.plus(1)
            }
        }
    }
}