package com.km.real_convenience_store.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.km.real_convenience_store.R
import com.km.real_convenience_store.database.AppDatabase
import com.km.real_convenience_store.databinding.ActivityMainBinding
import com.km.real_convenience_store.dto.local.FavoriteProductEntity
import com.km.real_convenience_store.main.adapter.FavoriteProductListAdapter
import com.km.real_convenience_store.main.adapter.FavoriteProductListDecorator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val favoriteProductListAdapter = FavoriteProductListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        CoroutineScope(Dispatchers.IO).launch {
            val favoriteProducts = AppDatabase.getInstance(this@MainActivity).userInfoDAO().getFavoriteProduct()
            favoriteProductListAdapter.addFavoriteProducts(favoriteProducts)
        }

        binding.rvFavoriteProduct.apply {
            adapter = FavoriteProductListAdapter()
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            addItemDecoration(FavoriteProductListDecorator())
        }
    }
}
