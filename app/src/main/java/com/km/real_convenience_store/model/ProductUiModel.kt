package com.km.real_convenience_store.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductUiModel(
    val storeName: String,
    val productImageUrl: String,
    val productName: String,
    val price: String,
    val saleType: String,
) : Parcelable
