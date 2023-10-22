package com.college.route

import com.college.data.repository.contact.ContactRepository
import com.college.middleware.Middleware
import com.college.model.request.contact.ContactRequest
import com.college.route.RouteResponseHelper.buildErrorJson
import com.college.route.RouteResponseHelper.buildSuccessJson
import com.college.route.RouteResponseHelper.buildSuccessListJson
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

class ContactRoute(private val repository: ContactRepository) {

    private fun Route.postContact() {
        authenticate {
            post("/user/{nim}/contact") {
                val body = try {
                    call.receive<ContactRequest>()
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                    return@post
                }

                val nim = call.parameters["nim"] ?: ""
                val isLimitReached = repository.isContactLimitReached(nim)

                if (isLimitReached) {
                    call.buildErrorJson(message = "Hanya dapat menambahkan 5 kontak darurat")
                    return@post
                } else {
                    try {
                        repository.insertContact(body, nim)
                        call.buildSuccessJson { "Berhasil menambahkan kontak darurat" }
                    } catch (e: Exception) {
                        call.buildErrorJson(message = "Gagal menambahkan kontak darurat")
                    }
                }
            }
        }
    }

    private fun Route.getContacts() {
        authenticate {
            get("/user/{uid}/contact") {
                val uid = call.parameters["uid"] ?: ""
                try {
                    val contacts = repository.getContactsByNim(uid)
                    call.buildSuccessListJson { contacts }
                } catch (e: Exception) {
                    call.buildErrorJson(e)
                }
            }
        }
    }

    private fun Route.deleteContact() {
        authenticate {
            delete("user/{uid}/contact/{contactId}") {
                try {
                    val contactId = call.parameters["contactId"] ?: ""
                    repository.deleteContact(contactId)
                    call.buildSuccessJson { "Berhasil menghapus kontak" }
                } catch (e: Exception) {
                    call.buildErrorJson(message = "Gagal menghapus kontak darurat")
                }
            }
        }
    }

    fun Route.initRoute() {
        postContact()
        deleteContact()
        getContacts()
    }

}