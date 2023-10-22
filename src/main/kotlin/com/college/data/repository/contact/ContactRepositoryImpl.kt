package com.college.data.repository.contact

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.college.data.database.DatabaseFactory
import com.college.data.table.ContactTable
import com.college.model.request.contact.ContactRequest
import com.college.model.response.contact.ContactResponse
import com.college.utils.toContact
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class ContactRepositoryImpl(private val dbFactory: DatabaseFactory) : ContactRepository {

    override suspend fun insertContact(body: ContactRequest, nim: String) {
        dbFactory.dbQuery {
            val idCreated = "CONTACT-${NanoIdUtils.randomNanoId()}"

            ContactTable.insert { table ->
                table[contactId] = idCreated
                table[uid] = nim
                table[name] = body.name
                table[number] = body.number
            }
        }
    }

    override suspend fun getContactsByNim(nim: String): List<ContactResponse> = dbFactory.dbQuery {
        ContactTable.select(ContactTable.uid.eq(nim)).mapNotNull { it.toContact() }
    }

    override suspend fun deleteContact(contactId: String) {
        dbFactory.dbQuery {
            ContactTable.deleteWhere { ContactTable.contactId.eq(contactId) }
        }
    }

    override suspend fun isContactLimitReached(userId: String): Boolean {

        val contactTotal = dbFactory.dbQuery {
            ContactTable.select(ContactTable.uid.eq(userId)).count()
        }

        return contactTotal >= 5
    }
}