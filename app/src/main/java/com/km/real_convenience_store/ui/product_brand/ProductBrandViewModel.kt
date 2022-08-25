package com.km.real_convenience_store.ui.product_brand

import android.util.Log
import android.view.View
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

    private val _changeSaleType = MutableLiveData<View?>()
    val changeSaleType: LiveData<View?>
        get() = _changeSaleType

    var needLoadMore: Boolean = true
    private var saleType: String? = null

    fun searchProduct(productName: String?, currentPage: Int, convenienceStoreName: String?) {
        viewModelScope.launch {
            if (!needLoadMore) return@launch

            val productDto = NetworkModule.convenienceStoreApi.getProducts(
                title = productName,
                saleType = saleType,
                page = currentPage,
                store = convenienceStoreName,
            )

            if (currentPage == productDto.pageData.maxPage) {
                needLoadMore = false
            }

            _searchProducts.value = productDto.data.map {
                it.toProductUiModel()
            }
        }
    }

    fun setSaleType(inputSaleType: String, view: View) {
        if (saleType != inputSaleType) {
            saleType = inputSaleType
            _changeSaleType.value = view
        } else {
            saleType = null
            _changeSaleType.value = null
        }
    }
}