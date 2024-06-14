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


data class Post(
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
    val Nickname: String,
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
    val userId: String,
    val categoryId: String,
    val title: String,
    val content: String,
)

data class InquiryMonthlyInfo (
    val id: String,
    val year: Int,
    val month: Int
)

data class MonthlyInfo (
    val recordId: Int,
    val USERId: String,
    val date: String,
    val dailyTime: Int
)

data class InquiryRank (
    val year: Int,
    val month: Int
)

data class RankInfo (
    val Nickname: String,
    val totalTime: Int
)

data class MyRank (
    val Nickname: String,
    val totalTime: Int,
    val rank: Int
)
data class PostList(
    val avatar: String,
    val Title: String,
    val Content: String,
    val Like: Int,
    val CommentCount: Int
)

data class ResponseListPost(
    val data: List<PostList>
)
