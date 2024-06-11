package com.example.dali_bike.models

data class User(
    var userId: String,
    var password: String,
    var phoneNumber: String,
    var name: String,
    var nickname: String,
    var points: Int,
    var subDate: String,
    var dailyTime: Int,
    var totalTime: Int
)

data class ID(
    val id: String
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
    val dailyTime: Int,
    val totalTime: Int
)

data class Register (
    val id: String,
    val pw: String,
    val phone: String,
    val name: String,
    val nickname: String
)

data class myInfo (
    val USERId: String,
    val Name: String,
    val Nickname: String,
    val Points: Int,
    val subDate: String
)

data class WritePost(
    val category: String,
    val title: String,
    val content: String,
    val userId: String
)