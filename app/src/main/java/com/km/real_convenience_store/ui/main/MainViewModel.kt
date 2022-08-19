package com.km.real_convenience_store.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.km.real_convenience_store.database.AppDatabase
import com.km.real_convenience_store.dto.local.FavoriteProductEntity
import com.km.real_convenience_store.model.ProductUiModel
import com.km.real_convenience_store.network.NetworkModule
import com.km.real_convenience_store.ui.product_search.toProductUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val database: AppDatabase,
) : ViewModel() {

    private val _products = MutableLiveData<List<ProductUiModel>>()
    val products: LiveData<List<ProductUiModel>>
        get() = _products

    private val _favoriteProducts = MutableLiveData<List<FavoriteProductEntity>>()
    val favoriteProducts: LiveData<List<FavoriteProductEntity>>
        get() = _favoriteProducts

    private var saleType: String? = null
    var currentPage = 1

    fun setSaleType(type: String?) {
        saleType =
            if (saleType.equals(type)) {
                null
            } else {
                type
            }
    }

    fun getSaleType(): String? = saleType

    fun getTotalProducts() {
        viewModelScope.launch {
            val productDto = NetworkModule.convenienceStoreApi.getProducts(
                saleType = saleType,
                page = currentPage
            )

            _products.value = productDto.data.map {
                it.toProductUiModel()
            }
        }
    }

    fun isFavoriteProducts() {
        CoroutineScope(Dispatchers.IO).launch {
            _favoriteProducts.postValue(
                database.userInfoDAO().getFavoriteProduct()
            )
        }
    }
}