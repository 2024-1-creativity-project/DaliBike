package com.example.dali_bike.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

class RecordViewModel : ViewModel() {
    private val _record = MutableLiveData<isRecordClicked>()
    val record: LiveData<isRecordClicked> get() = _record

    init {
        _record.value = isRecordClicked(
            startRecording = false
        )
    }

    fun setIsRecordClicked(startRecording: Boolean) {
        _record.value?.startRecording = startRecording
    }
}