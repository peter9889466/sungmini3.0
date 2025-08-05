package com.example.hackathon.data.model

data class StudyModel(
    val id: Long? = null,
    val name: String,
    val description: String,
    val category: String,
    val creatorId: Long,
    val creatorNickname: String,
    val maxMembers: Int = 10,
    val currentMembers: Int = 1,
    val status: String = "RECRUITING", // RECRUITING, FULL, CLOSED
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class CreateStudyRequest(
    val name: String,
    val description: String,
    val category: String,
    val maxMembers: Int = 10
)

data class StudyResponse(
    val success: Boolean,
    val message: String,
    val study: StudyModel? = null
)

data class StudyListResponse(
    val success: Boolean,
    val message: String,
    val studies: List<StudyModel> = emptyList(),
    val totalCount: Int = 0
)

data class SearchStudyRequest(
    val keyword: String,
    val category: String? = null,
    val page: Int = 0,
    val size: Int = 20
)