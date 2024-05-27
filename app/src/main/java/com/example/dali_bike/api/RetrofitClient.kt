package com.example.dali_bike.api

import com.example.dali_bike.models.LoginRequest
import com.example.dali_bike.models.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private const val BASE_URL = "http://172.30.74.3:3000"

interface ApiInterface {
    @POST("/user/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): User
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val mHttpLoggingInterceptor = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BODY)

private val mOkHttpClient = OkHttpClient.Builder()
    .addInterceptor(mHttpLoggingInterceptor)
    .build()

val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(mOkHttpClient)
    .build()

val apiService: ApiInterface = retrofit.create(ApiInterface::class.java)