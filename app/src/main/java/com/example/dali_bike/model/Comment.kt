package com.example.dali_bike.model

data class Comment(
    val postId: Int,
    val userId: String,
    val ment: String
)

data class getComment(
    val Nickname: String,
    val Comment: String
)

data class getPostId(
    val postId: Int
)

data class getResult(
    val result: String
)

data class like(
    val PostId: Int,
    val likeNum: Int
)

data class count(
    val Like: Int,
    val CommentCount: Int
)