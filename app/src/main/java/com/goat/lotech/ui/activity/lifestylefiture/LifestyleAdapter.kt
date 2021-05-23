package com.goat.lotech.ui.activity.lifestylefiture

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.goat.lotech.databinding.ItemLifestyleBinding
import com.goat.lotech.model.LifestyleModel
import com.goat.lotech.ui.activity.lifestylefiture.lifestyledetail.LifestyleDetailActivity

class LifestyleAdapter: RecyclerView.Adapter<LifestyleAdapter.LifestyleViewHolder>() {

    private var listLifestyle = ArrayList<LifestyleModel>()

    fun setLifestyle(lifestyle: List<LifestyleModel>) {
        if(lifestyle != null) {
            this.listLifestyle.clear()
            this.listLifestyle.addAll(lifestyle)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LifestyleViewHolder {
        val binding = ItemLifestyleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LifestyleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LifestyleViewHolder, position: Int) {
        val lifestyle = listLifestyle[position]
        holder.bind(lifestyle)
    }

    override fun getItemCount(): Int = listLifestyle.size

    class LifestyleViewHolder(private val binding: ItemLifestyleBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(lifestyle: LifestyleModel) {
            with(binding) {
                tvItemTitleLifestsyle.text = lifestyle.title
                tvItemSourceLifestsyle.text = lifestyle.source
                Glide.with(itemView.context)
                    .load(lifestyle.image)
                    .into(imgItemLifestyle)

                itemView.setOnClickListener{
                    val intent= Intent(itemView.context, LifestyleDetailActivity::class.java)
                    intent.putExtra(LifestyleDetailActivity.EXTRA_LIFESTYLE, lifestyle.lifestyleId)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}