package com.college.data.repository.user

import com.college.data.database.DatabaseFactory
import com.college.data.table.UserTable
import com.college.model.request.user.User
import com.college.model.request.user.UserRegisterRequest
import com.college.model.response.user.UserResponse
import com.college.security.hashing.SaltedHash
import com.college.utils.toUser
import com.college.utils.toUserResponse
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

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
}