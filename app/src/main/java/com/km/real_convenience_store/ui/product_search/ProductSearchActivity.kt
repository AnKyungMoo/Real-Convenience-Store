package com.km.real_convenience_store.ui.product_search

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.km.real_convenience_store.R
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

    private var saleType: String? = null
    private var currentPage: Int = 1
    private var needLoadMore: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()

        getFavoriteProduct()
    }

    private fun getFavoriteProduct() {
        val productName = intent.getStringExtra(PRODUCT_NAME)

        if(!productName.isNullOrEmpty()){
            binding.editProductSearch.setText(productName)
            needLoadMore = true
            currentPage = 1
            searchAndApplyProducts()
        }
    }

    private fun initViews() {
        binding.rvProducts.apply {
            adapter = productSearchAdapter
            layoutManager = LinearLayoutManager(this@ProductSearchActivity)

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

        binding.btnSearch.setOnClickListener {
            productSearchAdapter.clearProducts()
            needLoadMore = true
            currentPage = 1
            searchAndApplyProducts()
        }

        binding.btnOnePlusOne.setOnClickListener {
            resetSaleTypeButtonBackground()
            productSearchAdapter.clearProducts()
            needLoadMore = true
            currentPage = 1
            if (saleType != "1+1") {
                saleType = "1+1"
                changeSaleTypeButtonBackground(it)
            } else {
                saleType = null
            }
            searchAndApplyProducts()
        }
        binding.btnTwoPlusOne.setOnClickListener {
            resetSaleTypeButtonBackground()
            productSearchAdapter.clearProducts()
            needLoadMore = true
            currentPage = 1
            if (saleType != "2+1") {
                saleType = "2+1"
                changeSaleTypeButtonBackground(it)
            } else {
                saleType = null
            }
            searchAndApplyProducts()
        }
        binding.btnThreePlusOne.setOnClickListener {
            resetSaleTypeButtonBackground()
            productSearchAdapter.clearProducts()
            needLoadMore = true
            currentPage = 1
            if (saleType != "3+1") {
                saleType = "3+1"
                changeSaleTypeButtonBackground(it)
            } else {
                saleType = null
            }
            searchAndApplyProducts()
        }
        binding.btnFourPlusOne.setOnClickListener {
            resetSaleTypeButtonBackground()
            productSearchAdapter.clearProducts()
            needLoadMore = true
            currentPage = 1
            if (saleType != "4+1") {
                saleType = "4+1"
                changeSaleTypeButtonBackground(it)
            } else {
                saleType = null
            }
            searchAndApplyProducts()
        }
    }

    private fun searchAndApplyProducts() {
        if (!needLoadMore) return

        CoroutineScope(Dispatchers.Main).launch {
            val products: List<ProductUiModel> =
                searchProducts(binding.editProductSearch.text.toString())
            productSearchAdapter.addProducts(products)
            binding.tvSearchResultCount.text =
                "검색결과 ".plus("${productSearchAdapter.itemCount}").plus("건")
        }
    }

    private fun resetSaleTypeButtonBackground() {
        binding.btnOnePlusOne.apply {
            setBackgroundResource(R.drawable.bg_black_stroke)
            setTextColor(Color.BLACK)
        }
        binding.btnTwoPlusOne.apply {
            setBackgroundResource(R.drawable.bg_black_stroke)
            setTextColor(Color.BLACK)
        }
        binding.btnThreePlusOne.apply {
            setBackgroundResource(R.drawable.bg_black_stroke)
            setTextColor(Color.BLACK)
        }
        binding.btnFourPlusOne.apply {
            setBackgroundResource(R.drawable.bg_black_stroke)
            setTextColor(Color.BLACK)
        }
    }

    private fun changeSaleTypeButtonBackground(itemView: View) {
        itemView.setBackgroundResource(R.drawable.bg_sale_type_black_button)
        (itemView as TextView).setTextColor(Color.WHITE)
    }

    private suspend fun searchProducts(productName: String): List<ProductUiModel> {
        return withContext(Dispatchers.Default) {
            val productDto = NetworkModule.convenienceStoreApi.getProducts(
                title = productName,
                saleType = saleType,
                page = currentPage,
            )

            if (currentPage == productDto.pageData.maxPage) {
                needLoadMore = false
            }

            productDto.data.map {
                it.toProductUiModel()
            }
        }
    }

    companion object {
        const val PRODUCT_NAME = "PRODUCT_NAME"
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
