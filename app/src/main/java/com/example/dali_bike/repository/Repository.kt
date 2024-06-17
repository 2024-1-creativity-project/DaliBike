package com.example.dali_bike.repository

import android.content.Context
import android.widget.Toast
import com.example.dali_bike.model.Item
import com.example.dali_bike.RetrofitClient
import com.example.dali_bike.model.Comment
import com.example.dali_bike.model.Record
import com.example.dali_bike.model.RecordResult
import com.example.dali_bike.model.RecordUSERId
import com.example.dali_bike.model.Report
import com.example.dali_bike.model.ReportCancel
import com.example.dali_bike.model.ReportFileResult
import com.example.dali_bike.model.ReportResult
import com.example.dali_bike.model.count
import com.example.dali_bike.model.getComment
import com.example.dali_bike.model.getPostId
import com.example.dali_bike.model.getResult
import com.example.dali_bike.model.like
import com.example.dali_bike.model.lodgingDetailItem
import com.example.dali_bike.model.rentalDetailItem
import com.example.dali_bike.model.reportDetailItem
import com.example.dali_bike.model.storeDetailItem
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
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

    fun getReportDetail(onResult: (reportDetailItem?) -> Unit, onError: (Throwable) -> Unit, reportId : Int) {
        val call =RetrofitClient.apiService.getReportDetail(reportId)
        call.enqueue(object : Callback<reportDetailItem> {
            override fun onResponse(call: Call<reportDetailItem>, response: Response<reportDetailItem>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onError(Exception("Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<reportDetailItem>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun getReportImg(onResult: (ResponseBody?) -> Unit, onError: (Throwable) -> Unit, reportId : Int) {
        val call =RetrofitClient.apiService.getReportImg(reportId)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onError(Exception("Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun getLodgingDetailItem(onResult: (List<lodgingDetailItem>?) -> Unit, onError: (Throwable) -> Unit,  lodgingId : Int) {
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
    fun postViewToday(onResult: (List<Record>?) -> Unit, onError: (Throwable) -> Unit, recordUSERId: RecordUSERId) {
        val call =RetrofitClient.apiService.postViewToday(recordUSERId)
        call.enqueue(object : Callback<List<Record>> {
            override fun onResponse(call: Call<List<Record>>, response: Response<List<Record>>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onError(Exception("Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<Record>>, t: Throwable) {
                onError(t)
            }
        })
    }


    fun postRecord(onResult: (RecordResult?) -> Unit, onError: (Throwable) -> Unit, record: Record) {
        val call =RetrofitClient.apiService.postRecord(record)
        call.enqueue(object : Callback<RecordResult> {
            override fun onResponse(call: Call<RecordResult>, response: Response<RecordResult>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onError(Exception("Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<RecordResult>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun postReport(onResult: (ReportResult?) -> Unit, onError: (Throwable) -> Unit,report: Report, imageFile: MultipartBody.Part) {
        val id = report.userId.toRequestBody()
        val call =RetrofitClient.apiService.postReport(imageFile,id,report.type,report.latitude,report.longitude)
        call.enqueue(object : Callback<ReportResult> {
            override fun onResponse(call: Call<ReportResult>, response: Response<ReportResult>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onError(Exception("Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ReportResult>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun postReportCancel(onResult: (ReportResult?) -> Unit, onError: (Throwable) -> Unit, reportCancel: ReportCancel, imageFile: MultipartBody.Part) {
        val userId = reportCancel.userId.toRequestBody()

        val call =RetrofitClient.apiService.postReportCancel(imageFile,reportCancel.reportId, userId)
        call.enqueue(object : Callback<ReportResult> {
            override fun onResponse(call: Call<ReportResult>, response: Response<ReportResult>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onError(Exception("Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ReportResult>, t: Throwable) {
                onError(t)
            }
        })
    }
    fun postComment(onResult: (getResult?) -> Unit, onError: (Throwable) -> Unit, comment: Comment) {
        val call =RetrofitClient.apiService.postComment(comment)
        call.enqueue(object : Callback<getResult> {
            override fun onResponse(call: Call<getResult>, response: Response<getResult>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onError(Exception("Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<getResult>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun getComment(onResult: (List<getComment>?) -> Unit, onError: (Throwable) -> Unit, postId: getPostId) {
        val call =RetrofitClient.apiService.getComment(postId)
        call.enqueue(object : Callback<List<getComment>> {
            override fun onResponse(call: Call<List<getComment>>, response: Response<List<getComment>>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onError(Exception("Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<getComment>>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun postLike(onResult: (getResult?) -> Unit, onError: (Throwable) -> Unit, like: like) {
        val call =RetrofitClient.apiService.postLike(like)
        call.enqueue(object : Callback<getResult> {
            override fun onResponse(call: Call<getResult>, response: Response<getResult>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onError(Exception("Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<getResult>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun postCount(onResult: (List<count>?) -> Unit, onError: (Throwable) -> Unit, postId: getPostId) {
        val call =RetrofitClient.apiService.postCount(postId)
        call.enqueue(object : Callback<List<count>> {
            override fun onResponse(call: Call<List<count>>, response: Response<List<count>>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onError(Exception("Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<count>>, t: Throwable) {
                onError(t)
            }
        })
    }

}
