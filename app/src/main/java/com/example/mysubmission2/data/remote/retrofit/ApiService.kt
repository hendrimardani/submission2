package com.example.mysubmission2.data.remote.retrofit

import com.example.mysubmission2.data.remote.response.DetailResponse
import com.example.mysubmission2.data.remote.response.FinishedResponse
import com.example.mysubmission2.data.remote.response.NewNotificationResponse
import com.example.mysubmission2.data.remote.response.SearchResponse
import com.example.mysubmission2.data.remote.response.UpcomingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("events")
    fun getUpcoming(
        @Query("active") active: String = "1"
    ): Call<UpcomingResponse>

    @GET("events")
    fun getFinished(
        @Query("active") active: String = "0"
    ): Call<FinishedResponse>

    @GET("events")
    fun getSearch(
        @Query("active") active: String = "-1",
        @Query("q") q: String
    ): Call<SearchResponse>

    @GET("events/{id}")
    fun getDetail(
        @Path("id") id: String
    ): Call<DetailResponse>

    @GET("events")
    fun getNewNotification(
        @Query("active") active: String = "-1",
        @Query("limit") limit: String = "1"
    ): Call<NewNotificationResponse>
}