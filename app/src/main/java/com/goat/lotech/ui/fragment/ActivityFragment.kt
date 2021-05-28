package com.goat.lotech.ui.fragment

import android.os.Bundle
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
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        binding.rvHistory.layoutManager = LinearLayoutManager(activity)
        val adapter = ConsultHistoryAdapter(uid)
        adapter.notifyDataSetChanged()
        binding.rvHistory.adapter = adapter

        binding.progressBar.visibility = View.VISIBLE
        viewModel.setListHistory(uid)
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


}