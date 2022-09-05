package com.km.real_convenience_store.ui.product_detail

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

class DetailViewModel(
    private val database: AppDatabase,
) : ViewModel() {
    private val _isFavoriteProduct = MutableLiveData(false)
    val isFavoriteProduct: LiveData<Boolean>
        get() = _isFavoriteProduct

    private val _similarProducts = MutableLiveData<List<ProductUiModel>>()
    val similarProducts: LiveData<List<ProductUiModel>>
        get() = _similarProducts

    private val _switchFavoriteProductState = MutableLiveData<Boolean>()
    val switchFavoriteProductState: LiveData<Boolean>
        get() = _switchFavoriteProductState

    fun checkFavoriteProduct(productName: String?, storeName: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            _isFavoriteProduct.postValue(isFavoriteProduct(productName = productName, storeName = storeName))
        }
    }

    private suspend fun isFavoriteProduct(productName: String?, storeName: String?): Boolean {
        productName ?: return false
        storeName ?: return false

        return database
            .userInfoDAO()
            .isExistFavoriteProduct(productName, storeName) > 0
    }

    fun getSimilarProducts(currentPage: Int, productName: String?) {
        viewModelScope.launch {
            val products = NetworkModule.convenienceStoreApi.getProducts(
                page = currentPage,
                title = productName,
            ).data.map {
                it.toProductUiModel()
            }

            _similarProducts.value = products

            /* TODO: max page를 체크하는 방법에 대해 고민해보자.. (중요) */
//            if (currentPage == products.pageData.maxPage) {
//                needLoadMore = false
//            }
        }
    }

    fun onClickFavoriteButton(productName: String?, storeName: String?, imageUrl: String?) {
        viewModelScope.launch {
            if (isFavoriteProduct(productName, storeName)) {
                deleteFavoriteProduct(
                    productName = productName,
                    storeName = storeName,
                    imageUrl = imageUrl ?: "",
                )
                _switchFavoriteProductState.value = true
            } else {
                insertFavoriteProduct(
                    productName = productName ?: "",
                    storeName = storeName ?: "",
                    imageUrl = imageUrl ?: "",
                )
                _switchFavoriteProductState.value = false
            }
        }
    }

    private fun deleteFavoriteProduct(productName: String?, storeName: String?, imageUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            database.userInfoDAO().deleteFavoriteProduct(
                FavoriteProductEntity(
                    title = productName ?: "",
                    store = storeName ?: "",
                    imageUrl = imageUrl
                )
            )
        }
    }

    private fun insertFavoriteProduct(productName: String, storeName: String, imageUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            database.userInfoDAO().addFavoriteProduct(
                FavoriteProductEntity(
                    title = productName,
                    store = storeName,
                    imageUrl = imageUrl,
                )
            )
        }
    }
}
