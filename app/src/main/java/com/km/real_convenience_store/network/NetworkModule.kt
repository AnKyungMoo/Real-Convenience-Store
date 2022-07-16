package com.km.real_convenience_store.network

import com.km.real_convenience_store.network.service.ConvenienceStoreService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    val convenienceStoreApi: ConvenienceStoreService by lazy {
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl("http://211.202.74.84/")
            .build()
            .create(ConvenienceStoreService::class.java)
    }

    private val okHttpClient =
        OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor()).build()

}
