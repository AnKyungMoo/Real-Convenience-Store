package com.km.real_convenience_store.ui.product_brand

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.km.real_convenience_store.model.ProductUiModel
import com.km.real_convenience_store.network.NetworkModule
import com.km.real_convenience_store.ui.product_search.adapter.ProductSearchAdapter
import com.km.real_convenience_store.ui.product_search.toProductUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductBrandViewModel : ViewModel() {

    private val _searchProducts = MutableLiveData<List<ProductUiModel>>()
    val searchProducts: LiveData<List<ProductUiModel>>
        get() = _searchProducts

    var needLoadMore: Boolean = true

    //private var currentPage: Int = 1

    fun searchProduct(productName: String?, saleType: String?, currentPage: Int ,convenienceStoreName: String?) {
        viewModelScope.launch {
            if (!needLoadMore) return@launch

            val productDto = NetworkModule.convenienceStoreApi.getProducts(
                title = productName,
                saleType = saleType,
                page = currentPage,
                store = convenienceStoreName,
            )

            Log.d("@@Test", productDto.toString())

            if (currentPage == productDto.pageData.maxPage) {
                needLoadMore = false
            }

            _searchProducts.value = productDto.data.map {
                it.toProductUiModel()
            }
        }
    }
}