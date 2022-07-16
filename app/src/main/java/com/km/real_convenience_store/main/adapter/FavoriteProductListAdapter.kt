package com.km.real_convenience_store.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.km.real_convenience_store.R
import com.km.real_convenience_store.databinding.ItemFavoriteProductBinding
import com.km.real_convenience_store.dto.local.FavoriteProductEntity

class FavoriteProductListAdapter(private val mList: List<FavoriteProductEntity>) :
    RecyclerView.Adapter<FavoriteProductListAdapter.FavoriteProductListViewHolder>() {

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
        holder.onBind(mList[position])
    }

    override fun getItemCount(): Int = mList.size

    class FavoriteProductListViewHolder(private val binding: ItemFavoriteProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: FavoriteProductEntity) {
            binding.tvProductName.text = item.title
        }
    }
}