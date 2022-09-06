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
import com.km.real_convenience_store.common.SaleType
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
    private val viewModel: ProductBrandViewModel by viewModels()
    private val productSearchAdapter = ProductSearchAdapter()

    private var convenienceStoreName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductBrandBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentData()
        bindView()
        observeData()
    }

    private fun getIntentData() {
        convenienceStoreName = intent.getStringExtra(CONVENIENCE_STORE_NAME)
    }

    private fun bindView() {
        binding.tvBrandTitle.text = convenienceStoreName

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
                        viewModel.currentPage++
                        viewModel.searchProduct(
                            productName = binding.editProductSearch.text.toString(),
                            convenienceStoreName = convenienceStoreName
                        )
                    }
                }
            })
        }

        binding.btnSearch.setOnClickListener {
            saleTypeButtonEvents("", null)
        }

        binding.btnOnePlusOne.setOnClickListener {
            saleTypeButtonEvents(SaleType.ONE_PLUS_ONE, it)
        }

        binding.btnTwoPlusOne.setOnClickListener {
            saleTypeButtonEvents(SaleType.TWO_PLUS_ONE, it)
        }

        binding.btnThreePlusOne.setOnClickListener {
            saleTypeButtonEvents(SaleType.THREE_PLUS_ONE, it)
        }

        binding.btnFourPlusOne.setOnClickListener {
            saleTypeButtonEvents(SaleType.FOUR_PLUS_ONE, it)
        }
    }

    private fun observeData() {
        viewModel.searchProducts.observe(this) { product ->
            productSearchAdapter.addProducts(product)
        }

        viewModel.changeSaleType.observe(this) { view ->
            changeSaleTypeButtonBackground(view)
        }
    }

    private fun saleTypeButtonEvents(saleType: String, view: View?) {
        resetSaleTypeButtonBackground()
        productSearchAdapter.clearProducts()
        viewModel.needLoadMore = true
        viewModel.currentPage = 1
        viewModel.setSaleType(saleType, view)
        viewModel.searchProduct(
            productName = binding.editProductSearch.text.toString(),
            convenienceStoreName = convenienceStoreName
        )
    }

    private fun resetSaleTypeButtonBackground() {
        setUnselectBackground(binding.btnOnePlusOne)
        setUnselectBackground(binding.btnTwoPlusOne)
        setUnselectBackground(binding.btnThreePlusOne)
        setUnselectBackground(binding.btnFourPlusOne)
    }

    private fun setUnselectBackground(view: TextView) {
        view.setBackgroundResource(R.drawable.bg_black_stroke)
        view.setTextColor(Color.BLACK)
    }

    private fun changeSaleTypeButtonBackground(itemView: View?) {
        if (itemView != null) {
            itemView.setBackgroundResource(R.drawable.bg_sale_type_black_button)
            (itemView as TextView).setTextColor(Color.WHITE)
        }
    }

    companion object {
        const val CONVENIENCE_STORE_NAME = "CONVENIENCE_STORE_NAME"
    }
}
