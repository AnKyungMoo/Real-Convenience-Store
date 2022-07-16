package com.km.real_convenience_store.main

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.km.real_convenience_store.R
import com.km.real_convenience_store.database.AppDatabase
import com.km.real_convenience_store.databinding.ActivityMainBinding
import com.km.real_convenience_store.dto.local.FavoriteProductEntity
import com.km.real_convenience_store.main.adapter.FavoriteProductListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        var favoriteProductList: List<FavoriteProductEntity>

        CoroutineScope(Dispatchers.IO).launch {
            favoriteProductList =
                AppDatabase.getInstance(this@MainActivity).userInfoDAO().getFavoriteProduct()

            binding.rvFavoriteProduct.apply {
                adapter = FavoriteProductListAdapter(favoriteProductList)
                layoutManager = LinearLayoutManager(
                    this@MainActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }
        }
    }
}
