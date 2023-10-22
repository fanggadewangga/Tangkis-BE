package com.college.data.repository.contact

import com.college.model.request.contact.ContactRequest
import com.college.model.response.contact.ContactResponse

interface ContactRepository {
    suspend fun insertContact(body: ContactRequest, nim: String) // clear
    suspend fun getContactsByNim(nim: String): List<ContactResponse> // clear
    suspend fun deleteContact(contactId: String) // clear
    suspend fun isContactLimitReached(userId: String): Boolean // clear
}