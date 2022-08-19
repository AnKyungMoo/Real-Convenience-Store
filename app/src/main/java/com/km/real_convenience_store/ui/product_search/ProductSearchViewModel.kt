package com.km.real_convenience_store.ui.product_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.km.real_convenience_store.model.ProductUiModel
import com.km.real_convenience_store.network.NetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductSearchViewModel : ViewModel(){

    private val _searchProducts = MutableLiveData<List<ProductUiModel>>()
    val searchProducts: LiveData<List<ProductUiModel>>
        get() = _searchProducts

    var needLoadMore: Boolean = true
    var currentPage: Int = 1
    private var saleType: String? = null

    fun setSaleType(type: String?) {
        saleType =
            if (saleType.equals(type)) {
                null
            } else {
                type
            }
    }

    fun getSaleType(): String? = saleType

    fun searchProducts(productName: String) {
        if (!needLoadMore) return

        viewModelScope.launch {
            val productDto = NetworkModule.convenienceStoreApi.getProducts(
                title = productName,
                saleType = saleType,
                page = currentPage,
            )

            checkEndPage(productDto.pageData.maxPage, currentPage)

            _searchProducts.value = productDto.data.map {
                it.toProductUiModel()
            }
        }
    }

    private fun checkEndPage(maxPage: Int?, currentPage: Int) {
        if (currentPage == maxPage) {
            needLoadMore = false
        }
    }
}