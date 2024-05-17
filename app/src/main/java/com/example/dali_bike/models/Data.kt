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
    val USERId: String,
    val Password: String
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

