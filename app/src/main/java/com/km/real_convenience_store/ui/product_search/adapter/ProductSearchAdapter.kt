package com.km.real_convenience_store.ui.product_search.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.km.real_convenience_store.databinding.ItemConvenienceProductBinding
import com.km.real_convenience_store.main.DetailActivity
import com.km.real_convenience_store.model.ProductUiModel

class ProductSearchAdapter: RecyclerView.Adapter<ProductSearchViewHolder>() {
    val products: List<ProductUiModel>
        get() = _products
    private val _products = mutableListOf<ProductUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSearchViewHolder {
        return ProductSearchViewHolder(
            ItemConvenienceProductBinding.inflate(
                parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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
        /* TODO: 글라이드 붙여서 합시다 */
        binding.ivProduct

        itemView.setOnClickListener {
            val intent = Intent(itemView.context, DetailActivity::class.java)
            intent.putExtra("item", item)
            itemView.context.startActivity(intent)
        }
    }
}
