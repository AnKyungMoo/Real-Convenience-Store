package com.km.real_convenience_store.ui.product_search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.km.real_convenience_store.databinding.ItemConvenienceProductBinding
import com.km.real_convenience_store.model.ProductUiModel

class ProductSearchAdapter: RecyclerView.Adapter<ProductSearchViewHolder>() {
    private val products = mutableListOf<ProductUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSearchViewHolder {
        return ProductSearchViewHolder(
            ItemConvenienceProductBinding.inflate(
                parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            )
        )
    }

    override fun onBindViewHolder(holder: ProductSearchViewHolder, position: Int) {
        holder.onBind(products[position])
    }

    override fun getItemCount(): Int = products.size

    fun setProducts(products: List<ProductUiModel>) {
        this.products.clear()
        this.products.addAll(products)
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
        /* TODO: 글라이드 붙여서 합시다 */
        binding.ivProduct
    }
}
