package com.college.utils

import com.college.data.table.ConsultationTable
import com.college.data.table.ContactTable
import com.college.data.table.ReportTable
import com.college.data.table.UserTable
import com.college.model.response.consultation.ConsultationResponse
import com.college.model.response.report.ReportResponse
import com.college.model.request.user.User
import com.college.model.response.contact.ContactResponse
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
    nim = this[ReportTable.uid],
    story = this[ReportTable.story],
    isNeedConsultation = this[ReportTable.isNeedConsultation],
    consultationId = this[ReportTable.consultationId],
    progress = if (this[ReportTable.progressIndex] == ReportProgress.PROCESS.index) ReportProgress.PROCESS.message else ReportProgress.DONE.message,
    date = this[ReportTable.updateDate]
)

fun ResultRow.toConsultation() = ConsultationResponse(
    consultationId = this[ConsultationTable.consultationId],
    uid = this[ConsultationTable.uid],
    story = this[ConsultationTable.story],
    counselorChoice = if (this[ConsultationTable.counselorChoice] == ConselorChoice.ULTKSP.index) ConselorChoice.ULTKSP.choice else ConselorChoice.SEBAYA.choice,
    consultationType = if (this[ConsultationTable.consultationType] == ConsultationType.ONLINE.index) ConsultationType.ONLINE.type else ConsultationType.OFFLINE.type,
    date = this[ConsultationTable.date],
    time = this[ConsultationTable.time]
)