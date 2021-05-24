package com.goat.lotech.viewmodel.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.goat.lotech.R
import com.goat.lotech.databinding.ItemConsultantFindBinding
import com.goat.lotech.model.ConsultantVerifyModel
import com.goat.lotech.ui.activity.ConsultFindDetailActivity

class ConsultantFindAdapter : RecyclerView.Adapter<ConsultantFindAdapter.ConsultantFindViewHolder>() {

    private val consultantFindList = ArrayList<ConsultantVerifyModel>()
    fun setData(items: ArrayList<ConsultantVerifyModel>) {
        consultantFindList.clear()
        consultantFindList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultantFindViewHolder {
        val binding = ItemConsultantFindBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConsultantFindViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConsultantFindViewHolder, position: Int) {
        holder.bind(consultantFindList[position])
    }

    override fun getItemCount(): Int = consultantFindList.size


    inner class ConsultantFindViewHolder(private val binding: ItemConsultantFindBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(list: ConsultantVerifyModel) {
            with(binding) {
                name.text = list.name
                description.text = list.description
                liked.text = list.like.toString() + " Orang"

                Glide.with(itemView.context)
                    .load(list.selfPhoto)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_face_24))
                    .error(R.drawable.ic_baseline_face_24)
                    .into(selfPhoto)

                itemView.setOnClickListener {
                    val intent = Intent(it.context, ConsultFindDetailActivity::class.java)
                    intent.putExtra(ConsultFindDetailActivity.EXTRA_USER, list)
                    it.context.startActivity(intent)
                }
            }
        }

    }
}