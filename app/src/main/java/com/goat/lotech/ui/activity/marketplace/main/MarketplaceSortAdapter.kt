package com.goat.lotech.ui.activity.marketplace.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.goat.lotech.databinding.ItemSortBinding

class MarketplaceSortAdapter(private val data: List<MarketplaceSortModel>)
    : RecyclerView.Adapter<MarketplaceSortAdapter.MarketplaceSortViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    inner class MarketplaceSortViewHolder(private val binding: ItemSortBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list: MarketplaceSortModel) {
            binding.btnSort.text = list.title
            binding.btnSort.setOnClickListener{
                onItemClickCallback.clickFavoriteButton(list.title)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketplaceSortViewHolder {
        val binding = ItemSortBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarketplaceSortViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarketplaceSortViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    interface OnItemClickCallback {
        fun clickFavoriteButton(title: String?)
    }

}




