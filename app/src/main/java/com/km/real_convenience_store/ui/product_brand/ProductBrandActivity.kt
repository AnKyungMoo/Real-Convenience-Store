package com.km.real_convenience_store.ui.product_brand

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.km.real_convenience_store.R
import com.km.real_convenience_store.common.ViewModelFactory
import com.km.real_convenience_store.databinding.ActivityProductBrandBinding
import com.km.real_convenience_store.model.ProductUiModel
import com.km.real_convenience_store.network.NetworkModule
import com.km.real_convenience_store.ui.product_search.adapter.ProductSearchAdapter
import com.km.real_convenience_store.ui.product_search.toProductUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductBrandActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductBrandBinding
    private val viewModel: ProductBrandViewModel by viewModels { ViewModelFactory(this) }

    private val productSearchAdapter = ProductSearchAdapter()

    private var saleType: String? = null
    private var currentPage: Int = 1
    private var needLoadMore: Boolean = true

    private lateinit var convenienceStoreName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductBrandBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentData()
        bindViews()
        initViews()
    }

    private fun getIntentData() {
        convenienceStoreName = intent.getStringExtra(CONVENIENCE_STORE_NAME) ?: ""
    }

    private fun bindViews() {
        binding.tvBrandTitle.text = convenienceStoreName

        binding.btnSearch.setOnClickListener {
            productSearchAdapter.clearProducts()
            needLoadMore = true
            currentPage = 1
            searchAndApplyProducts()
        }

        binding.btnOnePlusOne.setOnClickListener {
            saleTypeButtonClickEvents("1+1", it)
        }
        binding.btnTwoPlusOne.setOnClickListener {
            saleTypeButtonClickEvents("2+1", it)

        }
        binding.btnThreePlusOne.setOnClickListener {
            saleTypeButtonClickEvents("3+1", it)

        }
        binding.btnFourPlusOne.setOnClickListener {
            saleTypeButtonClickEvents("4+1", it)
        }
    }

    private fun initViews() {

        binding.rvProducts.apply {
            adapter = productSearchAdapter
            layoutManager = LinearLayoutManager(this@ProductBrandActivity)

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

    private fun saleTypeButtonClickEvents(saleTypeValue: String, view: View) {
        resetSaleTypeButtonBackground()
        productSearchAdapter.clearProducts()
        needLoadMore = true
        currentPage = 1
        checkSaleType(saleTypeValue, view)
        searchAndApplyProducts()
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

    private fun checkSaleType(saleTypeValue: String, view: View) {
        if (saleType != saleTypeValue) {
            saleType = saleTypeValue
            changeSaleTypeButtonBackground(view)
        } else {
            saleType = null
        }
    }

    private fun changeSaleTypeButtonBackground(itemView: View) {
        itemView.setBackgroundResource(R.drawable.bg_sale_type_black_button)
        (itemView as TextView).setTextColor(Color.WHITE)
    }

    private fun searchAndApplyProducts() {
        if (!needLoadMore) return

        CoroutineScope(Dispatchers.Main).launch {
            val products: List<ProductUiModel> =
                searchProducts(binding.editProductSearch.text.toString())
            productSearchAdapter.addProducts(products)
        }
    }

    private suspend fun searchProducts(productName: String): List<ProductUiModel> {
        return withContext(Dispatchers.Default) {
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

    companion object {
        const val CONVENIENCE_STORE_NAME = "CONVENIENCE_STORE_NAME"
    }
}