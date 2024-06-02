package com.example.dali_bike.models

import java.util.Date

data class User(
    val userId: String,
    val password: String,
    val phoneNumber: String,
    val name: String,
    val nickname: String,
    val points: Int,
    val subDate: Date
)

data class LoginRequest (
    val id: String,
    val pw: String
)

data class Respon (
    val result: String
)

data class Register (
    val id: String,
    val pw: String,
    val phone: String,
    val name: String,
    val nickname: String
)

data class Store (
    val storeId: Int,
    val storeName: String,
    val storePhone: String,
    val localAddress: String,
    val roadAddress: String,
    val latitude: Double,
    val longitude: Double,
    val dayOff: String,
    val startTime: String,
    val endTime: String
)

data class StorageFacility (
    val sfId: Int,
    val latitude: Double,
    val longitude: Double
)

data class RemovalRequest (
    val reportId2: Int,
    val userId: String,
    val image: String
)

data class Report (
    val reportId: Int,
    val userId: String,
    val type: Boolean,
    val latitude: Double,
    val longitude: Double,
    val image: String
)

data class RentalStation (
    val rsId: Int,
    val rsName: String,
    val unmanRs: Boolean,
    val manRs: Boolean,
    val roadAddress: String,
    val localAddress: String,
    val latitude: Double,
    val longitude: Double,
    val startTime: String,
    val endTime: String,
    val dayOff: String,
    val isFare: Boolean,
    val fare: String,
    val managePhone: String
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

data class Lodging (
    val lodgingId: Int,
    val businessName: String,
    val locationPhoneNumber: String,
    val locationAddress: String,
    val locationPostcode: String,
    val roadAddress: String,
    val roadPostcode: String,
    val latitude: Double,
    val longitude: Double
)

data class Comment (
    val commentId: Int,
    val id: Int,
    val postId: Int,
    val comment: String
)

data class AirInjector (
    val aiId: Int,
    val latitude: Double,
    val longitude: Double
)