package com.goat.lotech.ui.activity.lifestylefiture

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goat.lotech.R
import com.goat.lotech.databinding.ItemLifestyleBinding
import com.goat.lotech.model.ArticlesItem
import com.goat.lotech.ui.activity.lifestylefiture.lifestyledetail.LifestyleDetailActivity

class LifestyleAdapter: RecyclerView.Adapter<LifestyleAdapter.LifestyleViewHolder>() {

    var listData = mutableListOf<ArticlesItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LifestyleViewHolder {
        val binding = ItemLifestyleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LifestyleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LifestyleViewHolder, position: Int) {
        val lifestyle = listData[position]
        holder.bind(lifestyle)
    }

    override fun getItemCount(): Int = listData.size

    class LifestyleViewHolder(private val binding: ItemLifestyleBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(lifestyle: ArticlesItem) {
            with(binding) {
                tvItemTitleLifestsyle.text = lifestyle.title
                source.text = lifestyle.source.name
                description.text = lifestyle.description
                Glide.with(itemView.context)
                    .load(lifestyle.urlToImage)
                    .placeholder(R.drawable.ic_baseline_refresh_24)
                    .error(R.drawable.ic_baseline_refresh_24)
                    .into(imageNews)

                itemView.setOnClickListener{
                    val intent= Intent(itemView.context, LifestyleDetailActivity::class.java)
                    intent.putExtra(LifestyleDetailActivity.EXTRA_LIFESTYLE, lifestyle.url)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}