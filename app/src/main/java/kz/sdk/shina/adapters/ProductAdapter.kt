package kz.sdk.shina.adapters



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import kz.sdk.shina.R
import kz.sdk.shina.base.BaseProductViewHolder
import kz.sdk.shina.databinding.ItemProductBinding
import kz.sdk.shina.models.Product

class ProductAdapter: ListAdapter<Product, BaseProductViewHolder<*>>(ProductDiffUtils()) {
    class ProductDiffUtils: DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    var itemClick:((Product) ->Unit)? = null

    inner class ProductViewHolder(binding:ItemProductBinding):
        BaseProductViewHolder<ItemProductBinding>(binding){
        override fun bindView(item: Product) {
            with(binding){
                Glide.with(itemView.context)
                    .load(item.img)
                    .placeholder(R.drawable.placeholder)
                    .into(img)
                name.text = item.title
                price.text = item.price.toString()+" ₸"
                credit.isVisible = item.isCredit
                view.isVisible = item.isCredit
                textView2.isVisible = item.isCredit
                creditPrice.text = (item.price?.div(72)).toString()+" ₸/мес"
                description.text = item.year.toString()+" г"+", ${item.type}, ${item.volume} л, ${item.transmission}, c пробегом ${item.millage} км, ${item.color}"
            }
            itemView.setOnClickListener {
                itemClick?.invoke(item)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseProductViewHolder<*> {
        return ProductViewHolder(
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseProductViewHolder<*>, position: Int) {
        holder.bindView(getItem(position))
    }
}
