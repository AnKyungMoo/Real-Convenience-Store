package com.km.real_convenience_store.ui.product_search

import android.graphics.Color
import android.os.Bundle
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

            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy);
                    val scrolledAdapter = recyclerView.adapter ?: return
                    val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
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
            searchAndApplyProducts()
        }

        binding.btnOnePlusOne.setOnClickListener {
            resetSaleTypeButtonBackground()
            currentPage = 1
            if (saleType != "1+1") {
                saleType = "1+1"
                changeSaleTypeButtonBackground(it)
            } else {
                saleType = null
            }
        }
        binding.btnTwoPlusOne.setOnClickListener {
            resetSaleTypeButtonBackground()
            currentPage = 1
            if (saleType != "2+1") {
                saleType = "2+1"
                changeSaleTypeButtonBackground(it)
            } else {
                saleType = null
            }
        }
        binding.btnThreePlusOne.setOnClickListener {
            resetSaleTypeButtonBackground()
            currentPage = 1
            if (saleType != "3+1") {
                saleType = "3+1"
                changeSaleTypeButtonBackground(it)
            } else {
                saleType = null
            }
        }
        binding.btnFourPlusOne.setOnClickListener {
            resetSaleTypeButtonBackground()
            currentPage = 1
            if (saleType != "4+1") {
                saleType = "4+1"
                changeSaleTypeButtonBackground(it)
            } else {
                saleType = null
            }
        }
    }

    private fun searchAndApplyProducts() {
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

    private suspend fun searchProducts(productName: String) =
        withContext(Dispatchers.Default) {
            NetworkModule.convenienceStoreApi.getProducts(
                title = productName,
                saleType = saleType,
                page = currentPage,
            ).data.map {
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
