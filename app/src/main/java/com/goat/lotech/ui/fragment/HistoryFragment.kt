package com.goat.lotech.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.goat.lotech.databinding.FragmentHistoryBinding
import com.goat.lotech.viewmodel.adapter.ConsultHistoryAdapter
import com.goat.lotech.viewmodel.viewmodel.ConsultHistoryViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HistoryFragment : Fragment() {

    private lateinit var binding : FragmentHistoryBinding
    private lateinit var viewModel: ConsultHistoryViewModel
    private val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ConsultHistoryViewModel::class.java]


        Firebase.firestore
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                if(it["role"].toString() == "super") {
                    showAllHistory()
                } else {
                    showData()
                }
            }

        return binding.root
    }

    private fun showAllHistory() {
        binding.rvHistory.layoutManager = LinearLayoutManager(activity)
        val adapter = ConsultHistoryAdapter(uid, "super")
        adapter.notifyDataSetChanged()
        binding.rvHistory.adapter = adapter

        viewModel.setListHistoryAll()
        viewModel.getListHistory().observe(viewLifecycleOwner, {
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

    private fun showData() {
        binding.rvHistory.layoutManager = LinearLayoutManager(activity)
        val adapter = ConsultHistoryAdapter(uid, "user")
        adapter.notifyDataSetChanged()
        binding.rvHistory.adapter = adapter

        Log.e("uid", uid)

        Firebase.firestore
            .collection("consultant")
            .document(uid)
            .get()
            .addOnSuccessListener {
                if(it.exists()) {
                    viewModel.setListHistory2(uid, "pakarUid")
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
                    viewModel.setListHistory2(uid, "userUid")
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