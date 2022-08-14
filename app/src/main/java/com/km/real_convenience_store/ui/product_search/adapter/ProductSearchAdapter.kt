package com.km.real_convenience_store.ui.product_search.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.km.real_convenience_store.common.StringValues
import com.km.real_convenience_store.databinding.ItemConvenienceProductBinding
import com.km.real_convenience_store.ui.product_detail.DetailActivity
import com.km.real_convenience_store.model.ProductUiModel

class ProductSearchAdapter: RecyclerView.Adapter<ProductSearchViewHolder>() {
    val products: List<ProductUiModel>
        get() = _products
    private val _products = mutableListOf<ProductUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSearchViewHolder {
        return ProductSearchViewHolder(
            ItemConvenienceProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductSearchViewHolder, position: Int) {
        holder.onBind(_products[position])
    }

    override fun getItemCount(): Int = _products.size

    fun addProducts(products: List<ProductUiModel>) {
        _products.addAll(products)
        notifyDataSetChanged()
    }

    fun clearProducts() {
        _products.clear()
        notifyDataSetChanged()
    }
}

class ProductSearchViewHolder(
    private val binding: ItemConvenienceProductBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(item: ProductUiModel) {
        binding.tvProductName.text = item.productName
        binding.tvConvenienceBrand.text = item.storeName
        binding.tvProductPrice.text = item.price
        binding.tvSaleType.text = item.saleType
        Glide.with(binding.ivProduct.context).load(item.productImageUrl).into(binding.ivProduct)

        itemView.setOnClickListener {
            val intent = Intent(itemView.context, DetailActivity::class.java)
            intent.putExtra(StringValues.INTENT_ITEM, item)
            itemView.context.startActivity(intent)
        }
    }
}
