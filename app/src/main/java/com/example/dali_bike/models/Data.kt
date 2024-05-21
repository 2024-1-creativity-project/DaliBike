package com.example.dali_bike.models

import java.util.Date

data class User(
    val USERId: String,
    val Password: String,
    val PhoneNumber: String,
    val Name: String,
    val Nickname: String,
    val Points: Int,
    val subDate: Date
)

data class LoginRequest (
    val id: String,
    val pw: String
)

data class Store (
    val StoreId: Int,
    val StoreName: String,
    val StorePhone: String,
    val LocalAddress: String,
    val RoadAddress: String,
    val Latitude: Double,
    val Longitude: Double,
    val DayOff: String,
    val StartTime: String,
    val EndTime: String
)

data class StorageFacility (
    val SFId: Int,
    val Latitude: Double,
    val Longitude: Double
)

data class RemovalRequest (
    val reportId2: Int,
    val USERId: String,
    val image: String
)

data class report (
    val reportId: Int,
    val USERId: String,
    val type: Boolean,
    val Latitude: Double,
    val Longitude: Double,
    val image: String
)

data class RentalStation (
    val RSId: Int,
    val RSName: String,
    val UnmanRs: Boolean,
    val ManRs: Boolean,
    val RoadAddress: String,
    val LocalAddress: String,
    val Latitude: Double,
    val Longitude: Double,
    val StartTime: String,
    val EndTime: String,
    val DayOff: String,
    val IsFare: Boolean,
    val Fare: String,
    val ManagePhone: String
)

data class record (
    val USERId: String,
    val date: Date,
    val dailyTime: Int
)

data class Post (
    val PostId: Int,
    val Title: String,
    val Content: String,
    val Like: Int,
    val category: String,
    val USERId: String
)

data class Lodging (
    val LodgingId: Int,
    val BusinessName: String,
    val LocationPhoneNumber: String,
    val LocationAddress: String,
    val LocationPostcode: String,
    val RoadAddress: String,
    val RoadPostcode: String,
    val Latitude: Double,
    val Longitude: Double
)

data class Comment (
    val CommentId: Int,
    val Id: Int,
    val PostId: Int,
    val Comment: String
)

data class AirInjector (
    val AIId: Int,
    val Latitude: Double,
    val Longitude: Double
)