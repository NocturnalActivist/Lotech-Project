package com.goat.lotech.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.goat.lotech.databinding.FragmentChatBinding
import com.goat.lotech.viewmodel.adapter.ChatAdapter
import com.goat.lotech.viewmodel.viewmodel.ChatViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var viewModel: ChatViewModel
    private val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ChatViewModel::class.java]
        showData()
        return binding.root
    }

    private fun showData() {
        binding.progressBar.visibility = View.VISIBLE
        Firebase.firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                val name = it["name"].toString()
                val image = it["image"].toString()
                showListDataChat(name, image)
            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                Log.e("TAG", "Fail load user data: ${it.printStackTrace()}")
                binding.noData.visibility = View.VISIBLE
            }
    }

    private fun showListDataChat(name: String, image: String) {


        binding.rvChat.layoutManager = LinearLayoutManager(activity)
        val adapter = ChatAdapter(name, image)
        adapter.notifyDataSetChanged()
        binding.rvChat.adapter = adapter

        viewModel.setItemList(uid)
        viewModel.getChatList().observe(viewLifecycleOwner, {
            if(it.size > 0) {
                binding.noData.visibility = View.GONE
                adapter.setData(it)
            }
            else {
                binding.noData.visibility = View.VISIBLE
            }
            binding.progressBar.visibility = View.GONE
        })
    }

}