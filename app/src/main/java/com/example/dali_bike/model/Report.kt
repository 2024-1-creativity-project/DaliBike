package com.example.dali_bike.model

import java.io.File

data class Report(
    val userId: String,
    val type: Int,
    val latitude: Double,
    val longitude: Double
)

data class ReportUSERId (
    val userId: String
)


data class ReportResult (
    val result: String
)

data class ReportFileResult (
    val result: File
)

data class ReportCancel(
    val userId: String,
    val reportId: Int
)