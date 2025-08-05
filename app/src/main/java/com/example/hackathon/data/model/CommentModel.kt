package com.example.hackathon.data.model

data class CommentModel(
    val id: Long? = null,
    val studyId: Long,
    val authorId: Long,
    val authorNickname: String,
    val content: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class CreateCommentRequest(
    val studyId: Long,
    val content: String
)

data class CommentResponse(
    val success: Boolean,
    val message: String,
    val comment: CommentModel? = null
)

data class CommentListResponse(
    val success: Boolean,
    val message: String,
    val comments: List<CommentModel> = emptyList(),
    val totalCount: Int = 0
)