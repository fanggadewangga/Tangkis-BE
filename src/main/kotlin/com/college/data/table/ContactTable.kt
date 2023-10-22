package com.college.data.table

import org.jetbrains.exposed.sql.Table

object ContactTable: Table() {
    override val tableName: String
        get() = "contact"

    val contactId = varchar("contact_id", 512)
    val uid = reference("user_id", UserTable.userId)
    val name = varchar("name", 128)
    val number = varchar("number", 32)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(contactId)
}