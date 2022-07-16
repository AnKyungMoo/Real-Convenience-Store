package com.km.real_convenience_store.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.km.real_convenience_store.database.dao.FavoriteProductDao
import com.km.real_convenience_store.dto.local.FavoriteProductEntity

@Database(entities = [FavoriteProductEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userInfoDAO(): FavoriteProductDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "convenience_store.db"
                ).build()
            }

            return instance!!
        }
    }
}
