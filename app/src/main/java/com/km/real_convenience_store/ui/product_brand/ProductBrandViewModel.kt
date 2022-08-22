package com.km.real_convenience_store.ui.product_brand

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.km.real_convenience_store.network.NetworkModule
import com.km.real_convenience_store.ui.product_search.toProductUiModel
import kotlinx.coroutines.launch

class ProductBrandViewModel: ViewModel() {

    private var currentPage = 1
    private var needLoadMore = true

    fun searchProduct(productName: String, saleType: String, convenienceStoreName: String) {
        viewModelScope.launch {

            val productDto = NetworkModule.convenienceStoreApi.getProducts(
                title = productName,
                saleType = saleType,
                page = currentPage,
                store = convenienceStoreName,
            )

            if (currentPage == productDto.pageData.maxPage) {
                needLoadMore = false
            }

            productDto.data.map {
                it.toProductUiModel()
            }
        }
    }
}