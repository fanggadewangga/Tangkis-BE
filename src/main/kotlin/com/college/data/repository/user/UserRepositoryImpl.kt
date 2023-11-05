package com.college.data.repository.user

import com.college.data.database.DatabaseFactory
import com.college.data.table.UserTable
import com.college.model.request.user.RPLUser
import com.college.model.request.user.User
import com.college.model.request.user.UserRPLRegisterRequest
import com.college.model.request.user.UserRegisterRequest
import com.college.model.response.user.RPLUserResponse
import com.college.model.response.user.UserResponse
import com.college.security.hashing.SaltedHash
import com.college.utils.toRPLUserResponse
import com.college.utils.toUser
import com.college.utils.toUserResponse
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class UserRepositoryImpl(
    private val dbFactory: DatabaseFactory
) : UserRepository {
    override suspend fun insertUser(body: UserRegisterRequest, saltedHash: SaltedHash): User {
        dbFactory.dbQuery {
            UserTable.insert { table ->
                table[userId] = body.nim
                table[name] = body.name
                table[whatsapp] = body.whatsapp
                table[password] = saltedHash.hash
                table[salt] = saltedHash.salt
            }
        }

        return User(
            nim = body.nim,
            name = body.name,
            whatsapp = body.whatsapp,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )
    }

    override suspend fun insertRPLUser(body: UserRPLRegisterRequest, saltedHash: SaltedHash): RPLUser {
        dbFactory.dbQuery {
            UserTable.insert { table ->
                table[userId] = body.nim
                table[name] = body.name
                table[whatsapp] = "0"
                table[password] = saltedHash.hash
                table[salt] = saltedHash.salt
            }
        }

        return RPLUser(
            nim = body.nim,
            name = body.name,
            password = saltedHash.hash,
        )
    }

    override suspend fun isIdentityNumberExist(nim: String): Boolean = dbFactory.dbQuery {
        UserTable.select { UserTable.userId eq nim }.count() > 0
    }

    override suspend fun getUserByIdentityNumber(nim: String): User? = dbFactory.dbQuery {
        UserTable.select { UserTable.userId eq nim }
            .mapNotNull {
                it.toUser()
            }
            .singleOrNull()
    }

    override suspend fun getUserDetail(userId: String): UserResponse = dbFactory.dbQuery {
        UserTable.select { UserTable.userId eq userId }
            .firstNotNullOf {
                it.toUserResponse()
            }
    }

    override suspend fun getRPLUserDetail(userId: String): RPLUserResponse = dbFactory.dbQuery {
        UserTable.select { UserTable.userId eq userId }
            .firstNotNullOf {
                it.toRPLUserResponse()
            }
    }

    override suspend fun updateUserWhatsapp(userId: String, newWhatsapp: String) {
        dbFactory.dbQuery {
            UserTable.update(where = { UserTable.userId.eq(userId) }) {
                it[whatsapp] = newWhatsapp
            }
        }
    }

    override suspend fun updateUserPassword(userId: String, saltedHash: SaltedHash) {
        dbFactory.dbQuery {
            UserTable.update(where = { UserTable.userId.eq(userId) }) {
                it[password] = saltedHash.hash
                it[salt] = saltedHash.salt
            }
        }
    }
}