package com.example.dali_bike.models

import java.util.Date

data class User(
    val userId: String,
    val password: String,
    val phoneNumber: String,
    var name: String,
    var nickname: String,
    var points: Int,
    val subDate: Date,
    var dailyTime: Int
)

data class LoginRequest (
    val id: String,
    val pw: String
)

data class Respon (
    val result: String
)

data class mainInfo (
    val nickname: String,
    val dailyTime: Int
)

data class Register (
    val id: String,
    val pw: String,
    val phone: String,
    val name: String,
    val nickname: String
)


data class Report (
    val reportId: Int,
    val userId: String,
    val type: Boolean,
    val latitude: Double,
    val longitude: Double,
    val image: String
)

data class Record (
    val userId: String,
    val date: Date,
    val dailyTime: Int
)

data class Post (
    val postId: Int,
    val title: String,
    val content: String,
    val like: Int,
    val category: String,
    val userId: String
)
data class Comment (
    val commentId: Int,
    val id: Int,
    val postId: Int,
    val comment: String
)
