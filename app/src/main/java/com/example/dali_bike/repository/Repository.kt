package com.example.dali_bike.repository

import android.widget.Toast
import com.example.dali_bike.model.Item
import com.example.dali_bike.RetrofitClient
import com.example.dali_bike.model.lodgingDetailItem
import com.example.dali_bike.model.rentalDetailItem
import com.example.dali_bike.model.storeDetailItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Repository {
    private var callItem = RetrofitClient.apiService.getAirInjector()
    private var itemNum : Int = 0

    fun getItem(onResult: (List<Item>?) -> Unit, onError: (Throwable) -> Unit, itemNum : Int) {
        getItemService(itemNum)
        callItem.enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onError(Exception("Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                onError(t)
            }
        })
    }

    private fun getItemService(itemNum : Int) {
        when(itemNum){
            1 -> callItem = RetrofitClient.apiService.getReports()
            2 -> callItem = RetrofitClient.apiService.getStore()
            3 -> callItem = RetrofitClient.apiService.getAirInjector()
            4 -> callItem = RetrofitClient.apiService.getLodging()
            5 -> callItem = RetrofitClient.apiService.getRental()
            6 -> callItem = RetrofitClient.apiService.getStorageFacility()
        }
    }


    fun getLodgingDetailItem(onResult: (List<lodgingDetailItem>?) -> Unit, onError: (Throwable) -> Unit, lodgingId : Int) {
        val call =RetrofitClient.apiService.getLodgingDetail(lodgingId)
        call.enqueue(object : Callback<List<lodgingDetailItem>> {
            override fun onResponse(call: Call<List<lodgingDetailItem>>, response: Response<List<lodgingDetailItem>>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onError(Exception("Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<lodgingDetailItem>>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun getRentalDetailItem(onResult: (List<rentalDetailItem>?) -> Unit, onError: (Throwable) -> Unit, rentalId : Int) {
        val call =RetrofitClient.apiService.getRentalDetail(rentalId)
        call.enqueue(object : Callback<List<rentalDetailItem>> {
            override fun onResponse(call: Call<List<rentalDetailItem>>, response: Response<List<rentalDetailItem>>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onError(Exception("Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<rentalDetailItem>>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun getStoreDetailItem(onResult: (List<storeDetailItem>?) -> Unit, onError: (Throwable) -> Unit, storeId : Int) {
        val call =RetrofitClient.apiService.getStoreDetail(storeId)
        call.enqueue(object : Callback<List<storeDetailItem>> {
            override fun onResponse(call: Call<List<storeDetailItem>>, response: Response<List<storeDetailItem>>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onError(Exception("Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<storeDetailItem>>, t: Throwable) {
                onError(t)
            }
        })
    }


}
