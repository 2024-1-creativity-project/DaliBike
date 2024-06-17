package com.example.dali_bike

import com.example.dali_bike.model.Comment
import com.example.dali_bike.model.Item
import com.example.dali_bike.model.Record
import com.example.dali_bike.model.RecordResult
import com.example.dali_bike.model.RecordUSERId
import com.example.dali_bike.model.Report
import com.example.dali_bike.model.ReportFileResult
import com.example.dali_bike.model.ReportResult
import com.example.dali_bike.model.count
import com.example.dali_bike.model.getComment
import com.example.dali_bike.model.getPostId
import com.example.dali_bike.model.getResult
import com.example.dali_bike.model.like
import com.example.dali_bike.model.lodgingDetailItem
import com.example.dali_bike.model.mainHotPost
import com.example.dali_bike.model.rentalDetailItem
import com.example.dali_bike.model.reportDetailItem
import com.example.dali_bike.model.storeDetailItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface RetrofitService {

    @GET("map/reports")
    fun getReports(): Call<List<Item>>

    @GET("map/store")
    fun getStore(): Call<List<Item>>

    @GET("map/air")
    fun getAirInjector(): Call<List<Item>>

    @GET("map/lodging")
    fun getLodging(): Call<List<Item>>

    @GET("map/rental")
    fun getRental(): Call<List<Item>>

    @GET("map/storage")
    fun getStorageFacility(): Call<List<Item>>

    // 여기에서 @Path를 사용하여 itemNum을 동적으로 URL에 삽입
    @GET("report/details/{itemNum}")
    fun getReportDetail(@Path("itemNum") itemNum: Int): Call<reportDetailItem>

    @GET("report/details/image/{itemNum}")
    fun getReportImg(@Path("itemNum") itemNum: Int): Call<ResponseBody>


    @GET("map/store/{itemNum}")
    fun getStoreDetail(@Path("itemNum") itemNum: Int): Call<List<storeDetailItem>>

    @GET("map/lodging/{itemNum}")
    fun getLodgingDetail(@Path("itemNum") itemNum: Int): Call<List<lodgingDetailItem>>

    @GET("map/rental/{itemNum}")
    fun getRentalDetail(@Path("itemNum") itemNum: Int): Call<List<rentalDetailItem>>

    @POST("record/my/today")
    fun postViewToday(@Body recordUSERId: RecordUSERId): Call<List<Record>>

    @POST("record/record")
    fun postRecord(@Body record: Record): Call<RecordResult>

    @Multipart
    @POST("report/add")
    fun postReport( @Part imageFile: MultipartBody.Part,
                    @Part("userId") userId: RequestBody,
                    @Part("type") type: Int,
                    @Part("latitude") latitude: Double,
                    @Part("longitude") longitude: Double): Call<ReportResult>

    @Multipart
    @POST("report/addRemoval")
    fun postReportCancel( @Part imageFile: MultipartBody.Part,
                          @Part("reportId") reportId: Int,
                          @Part("userId") userId: RequestBody): Call<ReportResult>
    @POST("comment/writeComment")
    fun postComment(@Body comment: Comment): Call<getResult>
    @POST("comment/getComment")
    fun getComment(@Body postId: getPostId): Call<List<getComment>>

    @POST("post/like")
    fun postLike(@Body like: like): Call<getResult>

    @POST("post/get/likeCommentAmount")
    fun postCount(@Body postId: getPostId): Call<List<count>>

}
