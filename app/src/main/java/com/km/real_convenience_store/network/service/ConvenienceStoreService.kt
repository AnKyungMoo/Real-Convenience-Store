package com.km.real_convenience_store.network.service

import com.km.real_convenience_store.dto.remote.ProductsResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface ConvenienceStoreService {
    @GET("api/conveniences/products/")
    suspend fun getProducts(
        @Query("month") month: String? = null,
        @Query("page") page: Int? = null,
        @Query("sale_type") saleType: String? = null,
        @Query("size") size: Int? = null,
        @Query("store") store: String? = null,
        @Query("title") title: String? = null,
        @Query("year") year: String? = null,
    ): ProductsResponseBody
}
