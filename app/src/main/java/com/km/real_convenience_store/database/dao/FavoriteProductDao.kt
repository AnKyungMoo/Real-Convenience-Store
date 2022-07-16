package com.km.real_convenience_store.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.km.real_convenience_store.dto.local.FavoriteProductEntity

@Dao
interface FavoriteProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavoriteProduct(product: FavoriteProductEntity)
}
