package com.example.dali_bike.model

data class Record (
    val id: String,
    val dailyTime: Int
)

data class RecordUSERId (
    val id: String
)

data class RecordResult (
    val result: String
)

data class isRecordClicked (
    var startRecording: Boolean
)