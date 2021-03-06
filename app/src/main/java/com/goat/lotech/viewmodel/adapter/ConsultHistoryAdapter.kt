package com.goat.lotech.viewmodel.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.goat.lotech.databinding.ItemConsultHistoryBinding
import com.goat.lotech.model.AddConsultant
import com.goat.lotech.model.ConsultHistoryModel
import com.goat.lotech.ui.activity.ConsultPaymentProofActivity

class ConsultHistoryAdapter(private val uid: String, private val role: String) : RecyclerView.Adapter<ConsultHistoryAdapter.ConsultViewHolder>() {

    private val listHistory = ArrayList<ConsultHistoryModel>()
    fun setData(items: ArrayList<ConsultHistoryModel>) {
        listHistory.clear()
        listHistory.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultViewHolder {
        val binding = ItemConsultHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConsultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConsultViewHolder, position: Int) {
        holder.bind(listHistory[position])
    }

    override fun getItemCount(): Int = listHistory.size


    inner class ConsultViewHolder(private val binding: ItemConsultHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(list: ConsultHistoryModel) {
            with(binding) {
                pakarName.text = list.pakarName
                userName.text = list.userName
                status.text = list.pakarStatus
                dateTime.text = list.dateTime
                price.text = list.price

                if(role == "super"){
                    bayar.visibility = View.VISIBLE

                    view6.setOnClickListener {
                        val intent = Intent(itemView.context, ConsultPaymentProofActivity::class.java)
                        intent.putExtra(ConsultPaymentProofActivity.EXTRA_DOCUMENT, list.timeInMillis)
                        itemView.context.startActivity(intent)
                    }
                } else {
                    if(uid != list.pakarUid && list.userStatus == "Siap") {
                        accept.visibility = View.INVISIBLE
                        finish.visibility = View.VISIBLE
                    } else if (uid == list.pakarUid && list.pakarStatus == "Menunggu persetujuan") {
                        accept.visibility = View.VISIBLE
                        finish.visibility = View.INVISIBLE
                    } else if (uid == list.pakarUid && list.pakarStatus == "Siap") {
                        accept.visibility = View.INVISIBLE
                        finish.visibility = View.VISIBLE
                    }
                }


                accept.setOnClickListener {
                    AddConsultant.pakarReady(itemView.context, list.timeInMillis.toString())
                    accept.visibility = View.INVISIBLE
                    finish.visibility = View.VISIBLE
                    status.text = "siap"
                }

                finish.setOnClickListener {
                    if(uid != list.pakarUid) {
                        AddConsultant.finishConsult(itemView.context, list.timeInMillis.toString(), "userStatus")
                        finish.visibility = View.INVISIBLE
                    } else {
                        AddConsultant.finishConsult(itemView.context, list.timeInMillis.toString(), "pakarStatus")
                        finish.visibility = View.INVISIBLE
                    }
                }

                bayar.setOnClickListener {
                    AddConsultant.bayarPakar(itemView.context, list.timeInMillis.toString())
                    bayar.visibility = View.INVISIBLE
                }
            }
        }

    }
}