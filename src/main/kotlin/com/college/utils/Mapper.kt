package com.college.utils

import com.college.data.table.*
import com.college.model.response.consultation.ConsultationDetailResponse
import com.college.model.response.report.ReportResponse
import com.college.model.request.user.User
import com.college.model.response.article.ArticleListResponse
import com.college.model.response.article.ArticleResponse
import com.college.model.response.consultation.ConsultationListResponse
import com.college.model.response.contact.ContactResponse
import com.college.model.response.report.ReportDetailResponse
import com.college.model.response.user.UserResponse
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toUser() = User(
    nim = this[UserTable.userId],
    name = this[UserTable.name],
    whatsapp = this[UserTable.whatsapp],
    studyProgram = this[UserTable.studyProgram],
    password = this[UserTable.password],
    salt = this[UserTable.salt]
)

fun ResultRow.toUserResponse() = UserResponse(
    nim = this[UserTable.userId],
    name = this[UserTable.name],
    whatsapp = this[UserTable.whatsapp],
    studyProgram = this[UserTable.studyProgram],
    password = this[UserTable.password],
    salt = this[UserTable.salt]
)

fun ResultRow.toContact() = ContactResponse(
    contactId = this[ContactTable.contactId],
    name = this[ContactTable.name],
    number = this[ContactTable.number]
)

fun ResultRow.toReports() = ReportResponse(
    reportId = this[ReportTable.reportId],
    title = "Pelaporan ULTKSP",
    progress = if (this[ReportTable.progressIndex] == Progress.PROCESS.index) Progress.PROCESS.message else Progress.DONE.message,
    updatedAt = this[ReportTable.updateDate]
)

fun ResultRow.toReportDetail() = ReportDetailResponse(
    reportId = this[ReportTable.reportId],
    nim = this[ReportTable.uid],
    story = this[ReportTable.story],
    isNeedConsultation = this[ReportTable.isNeedConsultation],
    consultationId = this[ReportTable.consultationId],
    progress = if (this[ReportTable.progressIndex] == Progress.PROCESS.index) Progress.PROCESS.message else Progress.DONE.message,
    date = this[ReportTable.updateDate],
    name = this[UserTable.name],
    whatsapp = this[UserTable.whatsapp]
)

fun ResultRow.toConsultationList(): ConsultationListResponse {
    val counselorChoice = if (this[ConsultationTable.counselorChoice] == ConselorChoice.ULTKSP.index) ConselorChoice.ULTKSP.choice else ConselorChoice.SEBAYA.choice

    return ConsultationListResponse(
        consultationId = this[ConsultationTable.consultationId],
        title = "Konsultasi $counselorChoice",
        updatedAt = this[ConsultationTable.updateDate] ?: ""
    )
}

fun ResultRow.toConsultation(): ConsultationDetailResponse {

    val counselorChoice = if (this[ConsultationTable.counselorChoice] == ConselorChoice.ULTKSP.index) ConselorChoice.ULTKSP.choice else ConselorChoice.SEBAYA.choice
    val consultationType = if (this[ConsultationTable.consultationType] == ConsultationType.ONLINE.index) ConsultationType.ONLINE.type else ConsultationType.OFFLINE.type

    return ConsultationDetailResponse(
        consultationId = this[ConsultationTable.consultationId],
        title = "Konsultasi $counselorChoice",
        uid = this[ConsultationTable.uid],
        story = this[ConsultationTable.story],
        progress = if (this[ConsultationTable.progressIndex] == Progress.PROCESS.index) Progress.PROCESS.message else Progress.DONE.message,
        counselorChoice = counselorChoice,
        consultationType = consultationType,
        date = this[ConsultationTable.date],
        time = "${this[ConsultationTimeTable.startTime]} - ${this[ConsultationTimeTable.endTime]}",
        createdAt = this[ConsultationTable.postDate],
        updatedAt = this[ConsultationTable.updateDate]
    )
}

fun ResultRow.toArticles() = ArticleListResponse(
    articleId = this[ArticleTable.articleId],
    title = this[ArticleTable.title],
    content = this[ArticleTable.content],
    imageUrl = this[ArticleTable.imageUrl]
)

fun ResultRow.toArticleDetail() = ArticleResponse(
    articleId = this[ArticleTable.articleId],
    title = this[ArticleTable.title],
    content = this[ArticleTable.content],
    imageUrl = this[ArticleTable.imageUrl],
    date = this[ArticleTable.postDate]
)