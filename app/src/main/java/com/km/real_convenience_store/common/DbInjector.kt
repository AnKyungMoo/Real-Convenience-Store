package com.km.real_convenience_store.common

import android.content.Context
import com.km.real_convenience_store.database.AppDatabase

object DbInjector {
    fun provideDataBase(context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }
}
