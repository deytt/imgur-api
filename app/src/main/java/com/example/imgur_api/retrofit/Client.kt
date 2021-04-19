package com.example.imgur_api.retrofit

import com.example.imgur_api.retrofit.service.Service
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {
    private val BASE_URL = "https://api.imgur.com/3/"
    private var retrofit: Retrofit? = null

    fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    fun getService() = getRetrofit().create(Service::class.java)

}