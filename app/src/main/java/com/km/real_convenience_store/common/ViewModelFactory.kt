package com.km.real_convenience_store.common

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.km.real_convenience_store.ui.product_detail.DetailViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            DetailViewModel::class.java -> createDetailViewModel(context)
            else -> throw IllegalArgumentException()
        } as T
    }

    private fun createDetailViewModel(context: Context): DetailViewModel {
        return DetailViewModel(
            DbInjector.provideDataBase(context),
        )
    }
}
