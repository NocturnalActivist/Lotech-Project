package com.goat.lotech.ui.activity.marketplace.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.goat.lotech.R
import com.goat.lotech.databinding.ItemProductBinding

class MarketplaceMainAdapter : RecyclerView.Adapter<MarketplaceMainAdapter.MarketplaceMainViewHolder>() {

    private var listProduct = ArrayList<MarketplaceMainModel>()
    fun setData(items: List<MarketplaceMainModel>) {
        listProduct.clear()
        listProduct.addAll(items)
        notifyDataSetChanged()
    }


    inner class MarketplaceMainViewHolder(private val binding: ItemProductBinding ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list: MarketplaceMainModel) {
            with(binding) {
                productName.text = list.productName
                price.text = list.price.toString()
                productDesc.text = list.productDescription
                merchantName.text = list.merchantName
                rating.text = list.rating.toString()

                Glide.with(itemView.context)
                    .load(list.productDp)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_face_24))
                    .error(R.drawable.ic_baseline_face_24)
                    .into(productDp)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketplaceMainViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarketplaceMainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarketplaceMainViewHolder, position: Int) {
        holder.bind(listProduct[position])
    }

    override fun getItemCount(): Int  = listProduct.size
}