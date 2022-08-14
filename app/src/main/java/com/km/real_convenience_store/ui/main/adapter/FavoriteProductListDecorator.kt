package com.km.real_convenience_store.ui.main.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.km.real_convenience_store.extension.dpToPx

class FavoriteProductListDecorator : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        when (parent.getChildAdapterPosition(view)) {
            0 -> outRect.left = 20.dpToPx(parent.context)
            (state.itemCount - 1) -> {
                outRect.right = 20.dpToPx(parent.context)
                outRect.left = 12.dpToPx(parent.context)
            }
            else -> {
                outRect.left = 12.dpToPx(parent.context)
            }
        }
    }
}
