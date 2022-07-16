package com.km.real_convenience_store.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.km.real_convenience_store.R
import com.km.real_convenience_store.database.AppDatabase
import com.km.real_convenience_store.databinding.ActivityMainBinding
import com.km.real_convenience_store.main.adapter.FavoriteProductAdapter
import com.km.real_convenience_store.main.adapter.FavoriteProductListDecorator
import com.km.real_convenience_store.model.ProductUiModel
import com.km.real_convenience_store.ui.product_brand.ProductBrandActivity
import com.km.real_convenience_store.ui.product_search.ProductSearchActivity
import com.km.real_convenience_store.ui.product_search.adapter.ProductSearchAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val favoriteProductListAdapter = FavoriteProductAdapter()
    private val productSearchAdapter = ProductSearchAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        moveToProductSearch()
        connectFavoriteProductAdapter()
        moveTdProductBrand()

    }

    fun moveTdProductBrand() {
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

    private fun connectFavoriteProductAdapter() {
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

    private fun moveToProductSearch() {
        binding.tvInputHint.setOnClickListener {
            startActivity(Intent(this, ProductSearchActivity::class.java))
        }
    }
}
