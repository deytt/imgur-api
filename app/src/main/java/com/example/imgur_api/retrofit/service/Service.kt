package com.example.imgur_api.retrofit.service

import com.example.imgur_api.BuildConfig
import com.example.imgur_api.model.ImgurResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface Service {
    @Headers(
        "Authorization: Client-ID ${BuildConfig.CLIENT_ID}",
    )

    @GET("gallery/top/top/top/{page}/?showViral=true&mature=false&album_previews=true/")
    fun findTopWeekly(@Query("page") page: Int): Call<ImgurResponse>
}