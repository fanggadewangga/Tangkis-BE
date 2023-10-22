package com.college.data.repository.user

import com.college.model.request.user.User
import com.college.model.request.user.UserRegisterRequest
import com.college.model.response.user.UserResponse
import com.college.security.hashing.SaltedHash

interface UserRepository {
    suspend fun insertUser(body: UserRegisterRequest, saltedHash: SaltedHash): User  //  clear
    suspend fun isIdentityNumberExist(nim: String): Boolean // clear
    suspend fun getUserByIdentityNumber(nim: String): User? // clear
    suspend fun getUserDetail(userId: String): UserResponse // not clear
}