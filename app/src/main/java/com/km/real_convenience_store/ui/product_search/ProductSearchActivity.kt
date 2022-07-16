package com.km.real_convenience_store.ui.product_search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.km.real_convenience_store.databinding.ActivityProductSearchBinding

class ProductSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
