package com.km.real_convenience_store.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.km.real_convenience_store.databinding.ActivityDetailBinding
import com.km.real_convenience_store.dto.remote.ProductDTO
import com.km.real_convenience_store.model.ProductUiModel
import com.km.real_convenience_store.network.NetworkModule
import com.km.real_convenience_store.ui.product_search.adapter.ProductSearchAdapter
import com.km.real_convenience_store.ui.product_search.toProductUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val productSearchAdapter = ProductSearchAdapter()

    private var productName: String? = null
    var currentPage: Int = 1
    var needLoadMore: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = intent.getParcelableExtra<ProductUiModel>("item")
        if (item != null) {

            Glide.with(binding.ivProductImage.context).load(item.productImageUrl)
                .into(binding.ivProductImage);
            binding.tvConvenienceBrand.text = item.storeName
            binding.tvProductName.text = item.productName
            productName = item.productName
            binding.tvProductPrice.text = item.price + "원"
            binding.tvProductSaleType.text = item.saleType
        }
        initRecyclerView()
        searchAndApplyProducts()

    }

    private fun initRecyclerView() {
        binding.rvProducts.apply {
            adapter = productSearchAdapter
            layoutManager = LinearLayoutManager(this@DetailActivity)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val scrolledAdapter = recyclerView.adapter ?: return
                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemTotalCount = scrolledAdapter.itemCount - 1

                    if (lastVisibleItemPosition == itemTotalCount) {
                        currentPage++
                        searchAndApplyProducts()
                    }
                }
            })
        }
    }

    private fun searchAndApplyProducts() {
        if (!needLoadMore) return

        CoroutineScope(Dispatchers.Main).launch {
            val products: List<ProductUiModel> =
                getSimilarItem()
            productSearchAdapter.addProducts(products)
        }
    }

    private suspend fun getSimilarItem(): List<ProductUiModel> {
        return withContext(Dispatchers.Default) {
            val productDto = NetworkModule.convenienceStoreApi.getProducts(
                page = currentPage,
                title = productName,
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