package com.km.real_convenience_store.ui.product_search

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.km.real_convenience_store.R
import com.km.real_convenience_store.databinding.ActivityProductSearchBinding
import com.km.real_convenience_store.dto.remote.ProductDTO
import com.km.real_convenience_store.model.ProductUiModel
import com.km.real_convenience_store.network.NetworkModule
import com.km.real_convenience_store.ui.main.MainActivity
import com.km.real_convenience_store.ui.product_search.adapter.ProductSearchAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductSearchBinding
    private val productSearchAdapter = ProductSearchAdapter()
    private val viewModel: ProductSearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindViews()
        bindObserverData()
        getFavoriteProduct()
    }

    private fun bindObserverData() {
       viewModel.searchProducts.observe(this){ products ->
           productSearchAdapter.addProducts(products)
           binding.tvSearchResultCount.text =
               "검색결과 ".plus("${productSearchAdapter.itemCount}").plus("건")
       }
    }

    private fun getFavoriteProduct() {
        val productName = intent.getStringExtra(PRODUCT_NAME)

        if(!productName.isNullOrEmpty()){
            binding.editProductSearch.setText(productName)
            initPagingData()
            searchAndApplyProducts()
        }
    }

    private fun bindViews() {
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
                        viewModel.currentPage++
                        searchAndApplyProducts()
                    }
                }
            })
        }

        binding.btnSearch.setOnClickListener {
            productSearchAdapter.clearProducts()
            initPagingData()
            searchAndApplyProducts()
        }

        binding.btnOnePlusOne.setOnClickListener {
            initPagingData()
            productSearchAdapter.clearProducts()
            resetSaleTypeButtonBackground()

            if (viewModel.getSaleType() != ONE_PLUS_ONE) {
                changeSaleTypeButtonBackground(it)
            }

            viewModel.setSaleType(ONE_PLUS_ONE)
            searchAndApplyProducts()
        }

        binding.btnTwoPlusOne.setOnClickListener {
            initPagingData()
            productSearchAdapter.clearProducts()
            resetSaleTypeButtonBackground()

            if (viewModel.getSaleType() != TWO_PLUS_ONE) {
                changeSaleTypeButtonBackground(it)
            }
            viewModel.setSaleType(TWO_PLUS_ONE)
            searchAndApplyProducts()
        }

        binding.btnThreePlusOne.setOnClickListener {
            initPagingData()
            productSearchAdapter.clearProducts()
            resetSaleTypeButtonBackground()

            if (viewModel.getSaleType() != THREE_PLUS_ONE) {
                changeSaleTypeButtonBackground(it)
            }
            viewModel.setSaleType(THREE_PLUS_ONE)
            searchAndApplyProducts()
        }

        binding.btnFourPlusOne.setOnClickListener {
            initPagingData()
            productSearchAdapter.clearProducts()
            resetSaleTypeButtonBackground()

            if (viewModel.getSaleType() != FOUR_PLUS_ONE) {
                changeSaleTypeButtonBackground(it)
            }
            viewModel.setSaleType(FOUR_PLUS_ONE)
            searchAndApplyProducts()
        }
    }

    private fun initPagingData() {
        viewModel.needLoadMore = true
        viewModel.currentPage = DEFAULT_PAGE
    }

    private fun searchAndApplyProducts() {
        viewModel.searchProducts(binding.editProductSearch.text.toString())
    }

    private fun resetSaleTypeButtonBackground() {
        binding.btnOnePlusOne.apply {
            initSaleTypeBackground(this)
        }

        binding.btnTwoPlusOne.apply {
            initSaleTypeBackground(this)
        }
        binding.btnThreePlusOne.apply {
            initSaleTypeBackground(this)
        }

        binding.btnFourPlusOne.apply {
            initSaleTypeBackground(this)
        }
    }

    private fun initSaleTypeBackground(itemView: View) {
        itemView.setBackgroundResource(R.drawable.bg_black_stroke)
        (itemView as TextView).setTextColor(Color.BLACK)
    }

    private fun changeSaleTypeButtonBackground(itemView: View) {
        itemView.setBackgroundResource(R.drawable.bg_sale_type_black_button)
        (itemView as TextView).setTextColor(Color.WHITE)
    }

    companion object {
        const val PRODUCT_NAME = "PRODUCT_NAME"
        const val ONE_PLUS_ONE = "1+1"
        const val TWO_PLUS_ONE = "2+1"
        const val THREE_PLUS_ONE = "3+1"
        const val FOUR_PLUS_ONE = "4+1"
        const val DEFAULT_PAGE = 1
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
