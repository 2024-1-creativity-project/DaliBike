package com.example.dali_bike.model

data class Item(
    val Id: Int,
    val ReportId: Int,
    val Latitude: Double,
    val Longitude: Double
)


data class lodgingDetailItem(
    val BusinessName: String,
    val LocationAddress: String,
    val LocationPhoneNumber: String,
    val LocationPostcode: String,
    val RoadAddress: String,
    val RoadPostcode: String
)
data class rentalDetailItem(
    val DayOff: String,
    val EndTime: String,
    val Fare: String,
    val IsFare: Int,
    val LocalAddress: String,
    val ManRS: Int,
    val ManagePhone: String,
    val RSName: String,
    val RoadAddress: String,
    val StartTime: String,
    val UnmanRS: Int
)
data class storeDetailItem(
    val DayOff: String,
    val EndTime: String,
    val LocalAddress: String,
    val RoadAddress: String,
    val StartTime: String,
    val StoreName: String,
    val StorePhone: String
)
data class reportDetailItem(
    val type: Int,
    val image: String
)