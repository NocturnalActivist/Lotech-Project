package com.goat.lotech.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.goat.lotech.databinding.FragmentActivityBinding
import com.goat.lotech.viewmodel.adapter.ConsultHistoryAdapter
import com.goat.lotech.viewmodel.viewmodel.ConsultHistoryViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ActivityFragment : Fragment() {

    private lateinit var binding: FragmentActivityBinding
    private lateinit var viewModel: ConsultHistoryViewModel
    private val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentActivityBinding.inflate(layoutInflater, container, false)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ConsultHistoryViewModel::class.java]
        showData()

        return binding.root
    }

    private fun showData() {
        binding.rvHistory.layoutManager = LinearLayoutManager(activity)
        val adapter = ConsultHistoryAdapter(uid, "user")
        adapter.notifyDataSetChanged()
        binding.rvHistory.adapter = adapter

        Log.e("UID", uid)
        binding.progressBar.visibility = View.VISIBLE

        Firebase.firestore
            .collection("consultant")
            .document(uid)
            .get()
            .addOnSuccessListener {
                if(it.exists()) {
                    Log.e("TAG", "ada")
                    viewModel.setListHistory(uid, "pakarUid")
                    viewModel.getListHistory().observe(viewLifecycleOwner, { data ->
                        if(data.size > 0) {
                            binding.noData.visibility = View.GONE
                            adapter.setData(data)
                        }
                        else {
                            binding.noData.visibility = View.VISIBLE
                        }
                        binding.progressBar.visibility = View.GONE
                    })
                } else {
                    Log.e("TAG", "bukan")
                    viewModel.setListHistory(uid, "userUid")
                    viewModel.getListHistory().observe(viewLifecycleOwner, { data ->
                        if(data.size > 0) {
                            binding.noData.visibility = View.GONE
                            adapter.setData(data)
                        }
                        else {
                            binding.noData.visibility = View.VISIBLE
                        }
                        binding.progressBar.visibility = View.GONE
                    })
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }
}