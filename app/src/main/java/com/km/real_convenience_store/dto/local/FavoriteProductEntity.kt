package com.km.real_convenience_store.dto.local

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["title", "store"], tableName = "favorite_product")
data class FavoriteProductEntity(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "store") val store: String,
    @ColumnInfo(name = "image_url") val imageUrl: String? = null,
)
