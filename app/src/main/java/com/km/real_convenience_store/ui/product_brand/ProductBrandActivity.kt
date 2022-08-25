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
    private val viewModel: ProductBrandViewModel by viewModels()
    private val productSearchAdapter = ProductSearchAdapter()

    private var currentPage: Int = 1

    private var convenienceStoreName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductBrandBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentData()
        bindView()
        initViews()
        observeData()
    }

    private fun getIntentData() {
        convenienceStoreName = intent.getStringExtra(CONVENIENCE_STORE_NAME)
    }

    private fun bindView() {
        binding.tvBrandTitle.text = convenienceStoreName
    }

    private fun observeData() {
        viewModel.searchProducts.observe(this) { product ->
            productSearchAdapter.addProducts(product)
        }

        viewModel.changeSaleType.observe(this) { view ->
            changeSaleTypeButtonBackground(view)
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
                        viewModel.searchProduct(
                            productName = binding.editProductSearch.text.toString(),
                            currentPage = currentPage,
                            convenienceStoreName = convenienceStoreName
                        )
                    }
                }
            })
        }

        binding.btnSearch.setOnClickListener {
            productSearchAdapter.clearProducts()
            viewModel.needLoadMore = true
            currentPage = 1
            viewModel.searchProduct(
                productName = binding.editProductSearch.text.toString(),
                currentPage = currentPage,
                convenienceStoreName = convenienceStoreName
            )
        }

        binding.btnOnePlusOne.setOnClickListener {
            resetSaleTypeButtonBackground()
            productSearchAdapter.clearProducts()
            viewModel.needLoadMore = true
            currentPage = 1
            viewModel.setSaleType("1+1", it)
            viewModel.searchProduct(
                productName = binding.editProductSearch.text.toString(),
                currentPage = currentPage,
                convenienceStoreName = convenienceStoreName
            )
        }
        binding.btnTwoPlusOne.setOnClickListener {
            resetSaleTypeButtonBackground()
            productSearchAdapter.clearProducts()
            viewModel.needLoadMore = true
            currentPage = 1
            viewModel.setSaleType("2+1", it)
            viewModel.searchProduct(
                productName = binding.editProductSearch.text.toString(),
                currentPage = currentPage,
                convenienceStoreName = convenienceStoreName
            )
        }
        binding.btnThreePlusOne.setOnClickListener {
            resetSaleTypeButtonBackground()
            productSearchAdapter.clearProducts()
            viewModel.needLoadMore = true
            currentPage = 1
            viewModel.setSaleType("3+1", it)
            viewModel.searchProduct(
                productName = binding.editProductSearch.text.toString(),
                currentPage = currentPage,
                convenienceStoreName = convenienceStoreName
            )
        }
        binding.btnFourPlusOne.setOnClickListener {
            resetSaleTypeButtonBackground()
            productSearchAdapter.clearProducts()
            viewModel.needLoadMore = true
            currentPage = 1
            viewModel.setSaleType("4+1", it)
            viewModel.searchProduct(
                productName = binding.editProductSearch.text.toString(),
                currentPage = currentPage,
                convenienceStoreName = convenienceStoreName
            )
        }
    }


    private fun resetSaleTypeButtonBackground() {
        binding.btnOnePlusOne.apply {
            setUnselectBackground(binding.btnOnePlusOne)

        }
        binding.btnTwoPlusOne.apply {
            setUnselectBackground(binding.btnTwoPlusOne)

        }
        binding.btnThreePlusOne.apply {
            setUnselectBackground(binding.btnThreePlusOne)

        }
        binding.btnFourPlusOne.apply {
            setUnselectBackground(binding.btnFourPlusOne)
        }
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
