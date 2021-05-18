package com.goat.lotech.viewmodel.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.goat.lotech.R
import com.goat.lotech.databinding.ItemConsultantVerifyBinding
import com.goat.lotech.model.ConsultantVerifyModel
import com.goat.lotech.ui.activity.ConsultVerifyDetailActivity

class ConsultantVerifyAdapter : RecyclerView.Adapter<ConsultantVerifyAdapter.ConsultantVerifyViewHolder>() {

    private val consultantVerifyList = ArrayList<ConsultantVerifyModel>()
    fun setData(items: ArrayList<ConsultantVerifyModel>) {
        consultantVerifyList.clear()
        consultantVerifyList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultantVerifyViewHolder {
        val binding = ItemConsultantVerifyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConsultantVerifyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConsultantVerifyViewHolder, position: Int) {
        holder.bind(consultantVerifyList[position])
    }

    override fun getItemCount(): Int = consultantVerifyList.size


    inner class ConsultantVerifyViewHolder(private val binding: ItemConsultantVerifyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list: ConsultantVerifyModel) {
            with(binding) {
                name.text = list.name
                description.text = list.description

                Glide.with(itemView.context)
                    .load(list.selfPhoto)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_face_24))
                    .error(R.drawable.ic_baseline_face_24)
                    .into(selfPhoto)

                itemView.setOnClickListener {
                    val intent = Intent(it.context, ConsultVerifyDetailActivity::class.java)
                    intent.putExtra(ConsultVerifyDetailActivity.EXTRA_USER, list)
                    it.context.startActivity(intent)
                }
            }
        }

    }


}