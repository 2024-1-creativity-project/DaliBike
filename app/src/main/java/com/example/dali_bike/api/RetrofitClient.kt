package com.example.dali_bike.api

import com.example.dali_bike.model.mainHotPost
import com.example.dali_bike.models.ID
import com.example.dali_bike.models.InquiryMonthlyInfo
import com.example.dali_bike.models.InquiryMyRank
import com.example.dali_bike.models.InquiryRank
import com.example.dali_bike.models.LoginRequest
import com.example.dali_bike.models.MonthlyInfo
import com.example.dali_bike.models.MyPost
import com.example.dali_bike.models.MyRank
import com.example.dali_bike.models.RankInfo
import com.example.dali_bike.models.Register
import com.example.dali_bike.models.Respon
import com.example.dali_bike.models.ResponseListPost
import com.example.dali_bike.models.WritePost
import com.example.dali_bike.models.mainInfo
import com.example.dali_bike.models.myInfo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Locale.Category
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

private const val BASE_URL = "http://172.30.101.87:3000"

interface ApiInterface {
    @POST("/user/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<List<Respon>>
    @GET("/user/redundancy/id/{id}")
    suspend fun checkId(@Path("id") id: String): Response<List<Respon>>

    @GET("/user/redundancy/nickname/{nickname}")
    suspend fun checkNickName(@Path("nickname") nickname: String): Response<List<Respon>>

    @POST("/user/register")
    suspend fun register(@Body register: Register): Response<Respon>

    @GET("post/view/hot")
    suspend fun getHotPost(): Response<List<mainHotPost>>

    @GET("post/list/{category}")
    suspend fun viewCategoryPost(@Path("category") category: String): ResponseListPost

    @POST("/user/main")
    suspend fun userMainInfo(@Body id: ID): Response<List<mainInfo>>

    @POST("/post/upload")
    suspend fun writePost(@Body writePost: WritePost): Response<Respon>

    @POST("/user/mypage")
    suspend fun myInfo(@Body id: ID): Response<List<myInfo>>

    @POST("/record/my/monthly")
    suspend fun myMonthly(@Body info: InquiryMonthlyInfo): Response<List<MonthlyInfo>>

    @POST("/record/rank")
    suspend fun rank(@Body info: InquiryRank): Response<List<RankInfo>>

    @POST("/record/my/rank")
    suspend fun myRank(@Body info: InquiryMyRank): Response<MyRank>

    @GET("/post/view/my/{userId}")
    suspend fun myPost(@Path("userId") userId: String): Response<List<MyPost>>

}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

fun mOkHttpClient(): OkHttpClient.Builder {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {}

        override fun checkServerTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {}

        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
            return emptyArray()
        }
    })

    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, java.security.SecureRandom())

    val sslSocketFactory = sslContext.socketFactory

    val builder = OkHttpClient.Builder()
    builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
    builder.hostnameVerifier{ _, _ -> true }

    return builder
}

val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(mOkHttpClient().build())
    .build()

val apiService: ApiInterface = retrofit.create(ApiInterface::class.java)

object MyApi {
    val retrofitService: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java) }
}