package com.km.real_convenience_store.ui.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.km.real_convenience_store.R
import com.km.real_convenience_store.common.ViewModelFactory
import com.km.real_convenience_store.databinding.ActivityMainBinding
import com.km.real_convenience_store.ui.main.adapter.FavoriteProductAdapter
import com.km.real_convenience_store.ui.main.adapter.FavoriteProductListDecorator
import com.km.real_convenience_store.ui.product_brand.ProductBrandActivity
import com.km.real_convenience_store.ui.product_search.ProductSearchActivity
import com.km.real_convenience_store.ui.product_search.adapter.ProductSearchAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels { ViewModelFactory(this) }
    private val favoriteProductListAdapter = FavoriteProductAdapter(this)
    private val productSearchAdapter = ProductSearchAdapter()

    private var currentPage: Int = DEFAULT_PAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        bindViews()
        observeData()
        initFavoriteProductRecyclerView()
        initTotalProductRecyclerView()

        viewModel.getTotalProducts(DEFAULT_PAGE)
    }

    private fun bindViews() {
        binding.tvOnePlusOne.setOnClickListener {
            productSearchAdapter.clearProducts()
            resetSaleTypeButtonBackground()

            if (viewModel.getSaleType() != ONE_PLUS_ONE) {
                changeSaleTypeButtonBackground(it)
            }

            viewModel.setSaleType(ONE_PLUS_ONE)
            viewModel.getTotalProducts(DEFAULT_PAGE)
        }

        binding.tvTwoPlusOne.setOnClickListener {
            productSearchAdapter.clearProducts()
            resetSaleTypeButtonBackground()

            if (viewModel.getSaleType() != TWO_PLUS_ONE) {
                changeSaleTypeButtonBackground(it)
            }
            viewModel.setSaleType(TWO_PLUS_ONE)
            viewModel.getTotalProducts(DEFAULT_PAGE)
        }

        binding.tvThreePlusOne.setOnClickListener {
            productSearchAdapter.clearProducts()
            resetSaleTypeButtonBackground()

            if (viewModel.getSaleType() != THREE_PLUS_ONE) {
                changeSaleTypeButtonBackground(it)
            }
            viewModel.setSaleType(THREE_PLUS_ONE)
            viewModel.getTotalProducts(DEFAULT_PAGE)
        }

        binding.tvFourPlusOne.setOnClickListener {
            productSearchAdapter.clearProducts()
            resetSaleTypeButtonBackground()

            if (viewModel.getSaleType() != FOUR_PLUS_ONE) {
                changeSaleTypeButtonBackground(it)
            }
            viewModel.setSaleType(FOUR_PLUS_ONE)
            viewModel.getTotalProducts(DEFAULT_PAGE)
        }

        binding.tvInputHint.setOnClickListener {
            startActivity(Intent(this, ProductSearchActivity::class.java))
        }

        binding.tvCu.setOnClickListener {
            val intent = Intent(this, ProductBrandActivity::class.java).apply {
                putExtra(ProductBrandActivity.CONVENIENCE_STORE_NAME, CU)
            }
            startActivity(intent)
        }

        binding.tvGs25.setOnClickListener {
            val intent = Intent(this, ProductBrandActivity::class.java).apply {
                putExtra(ProductBrandActivity.CONVENIENCE_STORE_NAME, GS_25)
            }
            startActivity(intent)
        }

        binding.tvSevenEleven.setOnClickListener {
            val intent = Intent(this, ProductBrandActivity::class.java).apply {
                putExtra(ProductBrandActivity.CONVENIENCE_STORE_NAME, SEVEN_ELEVEN)
            }
            startActivity(intent)
        }

        binding.tvEmart24.setOnClickListener {
            val intent = Intent(this, ProductBrandActivity::class.java).apply {
                putExtra(ProductBrandActivity.CONVENIENCE_STORE_NAME, E_MART_24)
            }
            startActivity(intent)
        }
    }

    private fun observeData() {
        viewModel.products.observe(this) { products ->
            productSearchAdapter.addProducts(products)
        }

        viewModel.favoriteProducts.observe(this) { favoriteProducts ->
            favoriteProductListAdapter.addFavoriteProducts(favoriteProducts)
            changeFavoriteProductView(favoriteProducts.isNotEmpty())
        }
    }

    private fun changeFavoriteProductView(isNotEmptyFavoriteProducts: Boolean) {
        binding.rvFavoriteProduct.visibility =
            if (isNotEmptyFavoriteProducts) View.VISIBLE else View.INVISIBLE
        binding.tvEmptyMessage.visibility =
            if (isNotEmptyFavoriteProducts) View.INVISIBLE else View.VISIBLE
    }

    private fun initFavoriteProductRecyclerView() {
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
                        viewModel.getTotalProducts(currentPage)
                    }
                }
            })
        }
    }

    private fun resetSaleTypeButtonBackground() {
        binding.tvOnePlusOne.apply {
            initSaleTypeBackground(this)
        }

        binding.tvTwoPlusOne.apply {
            initSaleTypeBackground(this)
        }

        binding.tvThreePlusOne.apply {
            initSaleTypeBackground(this)
        }

        binding.tvFourPlusOne.apply {
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

    override fun onResume() {
        super.onResume()
        getFavoriteProducts()
    }

    private fun getFavoriteProducts() {
        favoriteProductListAdapter.clearFavoriteProducts()
        viewModel.isFavoriteProducts()
    }

    companion object {
        const val CU = "CU"
        const val GS_25 = "GS25"
        const val SEVEN_ELEVEN = "Seven_eleven"
        const val E_MART_24 = "emart_24"
        const val DEFAULT_PAGE = 1
        const val ONE_PLUS_ONE = "1+1"
        const val TWO_PLUS_ONE = "2+1"
        const val THREE_PLUS_ONE = "3+1"
        const val FOUR_PLUS_ONE = "4+1"
    }
}
