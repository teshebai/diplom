package kz.sdk.shina.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kz.sdk.shina.models.Filter
import kz.sdk.shina.models.Product

abstract class BaseViewHolder<VB : ViewBinding, T>(protected open val binding: VB) :
    RecyclerView.ViewHolder(binding.root) {
    abstract fun bindView(item: T)
}

abstract class BaseProductViewHolder<VB : ViewBinding>(override val binding: VB) :
    BaseViewHolder<VB, Product>(binding)

abstract class BaseFilterViewHolder<VB : ViewBinding>(override val binding: VB) :
    BaseViewHolder<VB, Filter>(binding)