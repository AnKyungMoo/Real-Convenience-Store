package com.km.real_convenience_store.database.dao

import androidx.room.*
import com.km.real_convenience_store.dto.local.FavoriteProductEntity

@Dao
interface FavoriteProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteProduct(product: FavoriteProductEntity)

    @Query("SELECT * FROM favorite_product")
    suspend fun getFavoriteProduct(): List<FavoriteProductEntity>

    @Query("SELECT count(*) FROM favorite_product where title = :title AND store = :storeName")
    suspend fun isExistFavoriteProduct(title: String, storeName: String): Int

    @Delete
    suspend fun deleteFavoriteProduct(product: FavoriteProductEntity)
}
