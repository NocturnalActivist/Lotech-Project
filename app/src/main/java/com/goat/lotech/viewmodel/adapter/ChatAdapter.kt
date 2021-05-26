package com.goat.lotech.viewmodel.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.goat.lotech.R
import com.goat.lotech.databinding.ItemChatBinding
import com.goat.lotech.model.ChatModel
import com.goat.lotech.ui.activity.ChatUserActivity

class ChatAdapter(private val myName: String, private val mySelfPhoto: String) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private val messageList = ArrayList<ChatModel>()
    fun setData(items: ArrayList<ChatModel>) {
        messageList.clear()
        messageList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(messageList[position])
    }

    override fun getItemCount(): Int = messageList.size


    inner class ChatViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(list: ChatModel) {
            with(binding) {
                nameTv.text = list.name
                messageTv.text = list.lastMessage
                timeTv.text = list.lastMessageTime


                Glide.with(itemView.context)
                    .load(list.image)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_face_24))
                    .error(R.drawable.ic_baseline_face_24)
                    .into(dp)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, ChatUserActivity::class.java)
                    intent.putExtra(ChatUserActivity.NAME, list.name)
                    intent.putExtra(ChatUserActivity.SELF_PHOTO,list.image)
                    intent.putExtra(ChatUserActivity.UID, list.uid)
                    intent.putExtra(ChatUserActivity.MY_NAME, myName)
                    intent.putExtra(ChatUserActivity.MY_SELF_PHOTO, mySelfPhoto)
                    itemView.context.startActivity(intent)
                }

            }
        }

    }
}