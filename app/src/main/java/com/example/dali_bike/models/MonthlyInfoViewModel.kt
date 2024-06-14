package com.example.dali_bike.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MonthlyInfoViewModel : ViewModel() {
    private val _monthlyInfoList = MutableLiveData<List<MonthlyInfo>>()

    val monthlyInfoList: LiveData<List<MonthlyInfo>> get() = _monthlyInfoList

    init {
        _monthlyInfoList.value = emptyList()
    }

    fun updateMonthlyInfoList(newMonthlyInfoList: List<MonthlyInfo>) {
        _monthlyInfoList.value = newMonthlyInfoList
    }
}