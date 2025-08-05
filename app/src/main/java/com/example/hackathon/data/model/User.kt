package com.example.hackathon.data.model

data class User(
    val id: Long? = null,
    val userId: String,
    val password: String,
    val name: String,
    val nickname: String,
    val phone: String,
    val email: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class LoginRequest(
    val userId: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val user: User? = null,
    val token: String? = null
)

data class SignupRequest(
    val userId: String,
    val password: String,
    val name: String,
    val nickname: String,
    val phone: String,
    val email: String? = null
)