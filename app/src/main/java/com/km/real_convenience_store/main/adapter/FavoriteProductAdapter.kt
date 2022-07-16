package com.km.real_convenience_store.main.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.km.real_convenience_store.R
import com.km.real_convenience_store.databinding.ItemFavoriteProductBinding
import com.km.real_convenience_store.dto.local.FavoriteProductEntity
import com.km.real_convenience_store.ui.product_search.ProductSearchActivity

class FavoriteProductAdapter(private val context: Context) :
    RecyclerView.Adapter<FavoriteProductAdapter.FavoriteProductListViewHolder>() {

    private val favoriteProducts = mutableListOf<FavoriteProductEntity>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteProductListViewHolder = FavoriteProductListViewHolder(
        DataBindingUtil.inflate(
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
            R.layout.item_favorite_product,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: FavoriteProductListViewHolder, position: Int) {
        holder.onBind(favoriteProducts[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductSearchActivity::class.java).apply {
                putExtra(ProductSearchActivity.PRODUCT_NAME, favoriteProducts[position].title)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = favoriteProducts.size

    fun addFavoriteProducts(favoriteProducts: List<FavoriteProductEntity>) {
        this.favoriteProducts.addAll(favoriteProducts)
        notifyDataSetChanged()
    }

    class FavoriteProductListViewHolder(private val binding: ItemFavoriteProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: FavoriteProductEntity) {
            binding.tvProductName.text = item.title
            Glide.with(binding.ivProductImage.context).load(item.imageUrl).into(binding.ivProductImage)
        }
    }
}