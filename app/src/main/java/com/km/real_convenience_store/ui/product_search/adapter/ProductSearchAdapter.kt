package com.km.real_convenience_store.ui.product_search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.km.real_convenience_store.databinding.ItemConvenienceMerchandiseBinding
import com.km.real_convenience_store.model.ProductUiModel

class ProductSearchAdapter: RecyclerView.Adapter<ProductSearchViewHolder>() {
    private val products = mutableListOf<ProductUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSearchViewHolder {
        return ProductSearchViewHolder(
            ItemConvenienceMerchandiseBinding.inflate(
                parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            )
        )
    }

    override fun onBindViewHolder(holder: ProductSearchViewHolder, position: Int) {
        holder.onBind(products[position])
    }

    override fun getItemCount(): Int = products.size

    fun test() {
        val good = List(25) {
            ProductUiModel(
                storeName = "cu$it",
                eventPeriod = "1~5",
                productImageUrl = "",
                description = "설명설명cu",
                price = "${2000*it}"
            )
        }
        products.addAll(good)
    }
}

class ProductSearchViewHolder(
    private val binding: ItemConvenienceMerchandiseBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(item: ProductUiModel) {
        binding.tvConvenienceBrand.text = item.storeName
        binding.tvEventPeriod.text = item.eventPeriod
        binding.tvMerchandisePrice.text = item.price
        binding.tvMerchandiseDescription.text = item.description
        /* TODO: 글라이드 붙여서 합시다 */
        binding.ivMerchandise
    }
}
