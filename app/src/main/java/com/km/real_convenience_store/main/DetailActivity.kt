package com.km.real_convenience_store.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.km.real_convenience_store.databinding.ActivityDetailBinding
import com.km.real_convenience_store.model.ProductUiModel

class DetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = intent.getParcelableExtra<ProductUiModel>("item")
        if (item != null) {

//            binding.ivProductImage = item.productImageUrl
            binding.tvConvenienceBrand.text = item.storeName
            binding.tvProductName.text = item.productName
            binding.tvProductPrice.text = item.price +"원"
            binding.tvProductSaleType.text = item.saleType

        }
    }
}