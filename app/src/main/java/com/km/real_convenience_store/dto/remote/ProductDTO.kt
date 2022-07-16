package com.km.real_convenience_store.dto.remote

import com.google.gson.annotations.SerializedName

data class ProductsResponseBody(
    @SerializedName("page_data") val pageData: PageDataDTO,
    @SerializedName("data") val data: List<ProductDTO>
)

data class PageDataDTO(
    @SerializedName("count") val count: Int?,
    @SerializedName("next") val next: String?,
    @SerializedName("size") val size: Int?,
    @SerializedName("previous") val previous: String?,
)

data class ProductDTO(
    @SerializedName("id") val id: Int?,
    @SerializedName("store") val store: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("price") val price: Int?,
    @SerializedName("sale_type") val saleType: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("created_at") val createdAt: String?,
)
