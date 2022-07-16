package com.km.real_convenience_store.ui.product_search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.km.real_convenience_store.databinding.ActivityProductSearchBinding
import com.km.real_convenience_store.ui.product_search.adapter.ProductSearchAdapter

class ProductSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductSearchBinding
    private val productSearchAdapter = ProductSearchAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initProductSearchRecyclerView()
    }

    private fun initProductSearchRecyclerView() {
        binding.rvProducts.apply {
            adapter = productSearchAdapter
            layoutManager = LinearLayoutManager(this@ProductSearchActivity)
        }

        productSearchAdapter.test()
    }
}
