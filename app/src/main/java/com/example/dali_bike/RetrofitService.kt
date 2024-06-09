package com.example.dali_bike

import com.example.dali_bike.model.Item
import com.example.dali_bike.model.lodgingDetailItem
import com.example.dali_bike.model.rentalDetailItem
import com.example.dali_bike.model.storeDetailItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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


    @GET("post/hot")
    fun getHotPost(): Call<List<Item>>
}
