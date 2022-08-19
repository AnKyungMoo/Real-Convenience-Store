package com.km.real_convenience_store.ui.product_detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.km.real_convenience_store.R
import com.km.real_convenience_store.common.StringValues
import com.km.real_convenience_store.common.ViewModelFactory
import com.km.real_convenience_store.databinding.ActivityDetailBinding
import com.km.real_convenience_store.model.ProductUiModel
import com.km.real_convenience_store.ui.product_search.adapter.ProductSearchAdapter

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels { ViewModelFactory(this) }
    private val productSearchAdapter = ProductSearchAdapter()

    private var productName: String? = null
    private var storeName: String? = null
    private var imageUrl: String? = null
    private var price: String? = null
    private var saleType: String? = null

    var currentPage: Int = 1
    var needLoadMore: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentData()
        bindViews()
        initRecyclerView()
        getSimilarProducts()

        viewModel.checkFavoriteProduct(productName = productName, storeName = storeName)
        observeData()
    }

    private fun getIntentData() {
        intent.getParcelableExtra<ProductUiModel>(StringValues.INTENT_ITEM)?.let { product ->
            imageUrl = product.productImageUrl
            storeName = product.storeName
            productName = product.productName
            price = product.price
            saleType = product.saleType
        }
    }

    private fun bindViews() {
        Glide.with(binding.ivProductImage.context)
            .load(imageUrl)
            .into(binding.ivProductImage)

        binding.tvConvenienceBrand.text = storeName
        binding.tvProductName.text = productName
        binding.tvProductPrice.text = price + "원"
        binding.tvProductSaleType.text = saleType

        binding.btnFavorite.setOnClickListener {
            viewModel.onClickFavoriteButton(
                productName = productName,
                storeName = storeName,
                imageUrl = imageUrl,
            )
        }
    }

    private fun observeData() {
        viewModel.isFavoriteProduct.observe(this) {
            setFavoriteIcon(it)
        }
        viewModel.similarProducts.observe(this) { products ->
            productSearchAdapter.addProducts(products)
        }
        viewModel.switchFavoriteProductState.observe(this) {
            switchFavoriteProductState(it)
        }
    }

    private fun setFavoriteIcon(isActiveIcon: Boolean) {
        if (isActiveIcon) binding.btnFavorite.setImageResource(R.drawable.icon_black_star)
        else binding.btnFavorite.setImageResource(R.drawable.icon_white_star)
    }

    private fun initRecyclerView() {
        binding.rvProducts.apply {
            adapter = productSearchAdapter
            layoutManager = LinearLayoutManager(this@DetailActivity)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val scrolledAdapter = recyclerView.adapter ?: return
                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemTotalCount = scrolledAdapter.itemCount - 1

                    if (lastVisibleItemPosition == itemTotalCount) {
                        currentPage++
                        getSimilarProducts()
                    }
                }
            })
        }
    }

    /* TODO: currentPage를 파라미터로 넘겨야할까? */
    private fun getSimilarProducts() {
        if (!needLoadMore) return

        viewModel.getSimilarProducts(currentPage, productName)
    }

    private fun switchFavoriteProductState(isRemoveFavoriteProduct: Boolean) {
        setFavoriteIcon(isActiveIcon = isRemoveFavoriteProduct.not())
        showFavoriteToastMessage(isRemoveFavoriteProduct = isRemoveFavoriteProduct)
    }

    private fun showFavoriteToastMessage(isRemoveFavoriteProduct: Boolean) {
        Toast.makeText(
            this@DetailActivity,
            if (isRemoveFavoriteProduct) {
                "즐겨찾기를 해제했습니다"
            } else {
                "즐겨찾기에 추가했습니다"
            },
            Toast.LENGTH_SHORT,
        ).show()
    }
}
