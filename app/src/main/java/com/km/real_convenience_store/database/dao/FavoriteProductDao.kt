package com.km.real_convenience_store.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.km.real_convenience_store.dto.local.FavoriteProductEntity

@Dao
interface FavoriteProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteProduct(product: FavoriteProductEntity)

    @Query("SELECT * FROM favorite_product;")
    suspend fun getFavoriteProduct(): List<FavoriteProductEntity>
}
