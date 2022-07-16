package com.km.real_convenience_store.ui.product_search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.km.real_convenience_store.databinding.ActivityProductSearchBinding
import com.km.real_convenience_store.dto.remote.ProductDTO
import com.km.real_convenience_store.model.ProductUiModel
import com.km.real_convenience_store.network.NetworkModule
import com.km.real_convenience_store.ui.product_search.adapter.ProductSearchAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductSearchBinding
    private val productSearchAdapter = ProductSearchAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.rvProducts.apply {
            adapter = productSearchAdapter
            layoutManager = LinearLayoutManager(this@ProductSearchActivity)
        }

        binding.btnSearch.setOnClickListener {
            productSearchAdapter.clearProducts()

            CoroutineScope(Dispatchers.Main).launch {
                val products: List<ProductUiModel> =
                    searchProducts(binding.editProductSearch.text.toString())
                productSearchAdapter.setProducts(products)
                binding.tvSearchResultCount.text =
                    "검색결과".plus("${productSearchAdapter.itemCount}").plus("건")
            }
        }
    }

    private suspend fun searchProducts(productName: String) =
        withContext(Dispatchers.Default) {
            NetworkModule.convenienceStoreApi.getProducts(title = productName).data.map {
                it.toProductUiModel()
            }
        }
}

fun ProductDTO.toProductUiModel() =
    ProductUiModel(
        storeName = this.store ?: "",
        productImageUrl = this.image ?: "",
        productName = this.title ?: "",
        price = this.price.toString(),
        saleType = this.saleType ?: ""
    )
