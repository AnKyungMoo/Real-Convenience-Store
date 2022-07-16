package com.km.real_convenience_store.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.km.real_convenience_store.R
import com.km.real_convenience_store.database.AppDatabase
import com.km.real_convenience_store.databinding.ActivityMainBinding
import com.km.real_convenience_store.main.adapter.FavoriteProductAdapter
import com.km.real_convenience_store.main.adapter.FavoriteProductListDecorator
import com.km.real_convenience_store.model.ProductUiModel
import com.km.real_convenience_store.network.NetworkModule
import com.km.real_convenience_store.ui.product_brand.ProductBrandActivity
import com.km.real_convenience_store.ui.product_search.ProductSearchActivity
import com.km.real_convenience_store.ui.product_search.adapter.ProductSearchAdapter
import com.km.real_convenience_store.ui.product_search.toProductUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val favoriteProductListAdapter = FavoriteProductAdapter()
    private val productSearchAdapter = ProductSearchAdapter()
    private var needLoadMore: Boolean = true
    private var currentPage: Int = 1
    private var saleType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        selectEvent()
        moveToProductSearch()
        moveToProductBrand()
        initFavoriteProductRecyclerView()
        initTotalProductRecyclerView()
        getAndApplyTotalProduct()

    }

    private fun selectEvent() {
        binding.tvOnePlusOne.setOnClickListener {
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
            getAndApplyTotalProduct()
        }

        binding.tvTwoPlusOne.setOnClickListener {
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
            getAndApplyTotalProduct()
        }

        binding.tvThreePlusOne.setOnClickListener {
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
            getAndApplyTotalProduct()
        }

        binding.tvFourPlusOne.setOnClickListener {
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
            getAndApplyTotalProduct()
        }
    }

    private fun moveToProductSearch() {
        binding.tvInputHint.setOnClickListener {
            startActivity(Intent(this, ProductSearchActivity::class.java))
        }
    }

    private fun moveToProductBrand() {
        binding.tvCu.setOnClickListener {
            val intent = Intent(this, ProductBrandActivity::class.java).apply {
                putExtra(ProductBrandActivity.CONVENIENCE_STORE_NAME, "CU")
            }
            startActivity(intent)
        }

        binding.tvGs25.setOnClickListener {
            val intent = Intent(this, ProductBrandActivity::class.java).apply {
                putExtra(ProductBrandActivity.CONVENIENCE_STORE_NAME, "GS25")
            }
            startActivity(intent)
        }

        binding.tvSevenEleven.setOnClickListener {
            val intent = Intent(this, ProductBrandActivity::class.java).apply {
                putExtra(ProductBrandActivity.CONVENIENCE_STORE_NAME, "Seven_eleven")
            }
            startActivity(intent)
        }

        binding.tvEmart24.setOnClickListener {
            val intent = Intent(this, ProductBrandActivity::class.java).apply {
                putExtra(ProductBrandActivity.CONVENIENCE_STORE_NAME, "emart24")
            }
            startActivity(intent)
        }
    }

    private fun initFavoriteProductRecyclerView() {
        CoroutineScope(Dispatchers.IO).launch {
            val favoriteProducts =
                AppDatabase.getInstance(this@MainActivity).userInfoDAO().getFavoriteProduct()
            favoriteProductListAdapter.addFavoriteProducts(favoriteProducts)
        }

        binding.rvFavoriteProduct.apply {
            adapter = favoriteProductListAdapter
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            addItemDecoration(FavoriteProductListDecorator())
        }
    }



    private fun initTotalProductRecyclerView() {
        binding.rvTotalProduct.apply {
            adapter = productSearchAdapter
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.VERTICAL,
                false
            )

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val scrolledAdapter = recyclerView.adapter ?: return
                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemTotalCount = scrolledAdapter.itemCount - 1

                    if (lastVisibleItemPosition == itemTotalCount) {
                        currentPage++
                        getAndApplyTotalProduct()
                    }
                }
            })
        }
    }

    private fun getAndApplyTotalProduct() {
        if (!needLoadMore) return

        CoroutineScope(Dispatchers.Main).launch {
            val products: List<ProductUiModel> = getTotalProduct()
            productSearchAdapter.addProducts(products)
        }
    }

    private suspend fun getTotalProduct(): List<ProductUiModel> {
        return withContext(Dispatchers.Default) {
            val productDto = NetworkModule.convenienceStoreApi.getProducts(
                saleType = saleType,
                page = currentPage
            )

            productDto.data.map {
                it.toProductUiModel()
            }
        }
    }

    private fun resetSaleTypeButtonBackground() {
        binding.tvOnePlusOne.apply {
            setBackgroundResource(R.drawable.bg_black_stroke)
            setTextColor(Color.BLACK)
        }
        binding.tvTwoPlusOne.apply {
            setBackgroundResource(R.drawable.bg_black_stroke)
            setTextColor(Color.BLACK)
        }
        binding.tvThreePlusOne.apply {
            setBackgroundResource(R.drawable.bg_black_stroke)
            setTextColor(Color.BLACK)
        }
        binding.tvFourPlusOne.apply {
            setBackgroundResource(R.drawable.bg_black_stroke)
            setTextColor(Color.BLACK)
        }
    }

    private fun changeSaleTypeButtonBackground(itemView: View) {
        itemView.setBackgroundResource(R.drawable.bg_sale_type_black_button)
        (itemView as TextView).setTextColor(Color.WHITE)
    }
}