package com.goat.lotech.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.goat.lotech.databinding.ActivityConsultFindConsultantBinding
import com.goat.lotech.viewmodel.adapter.ConsultantFindAdapter
import com.goat.lotech.viewmodel.viewmodel.ConsultantFindViewModel

class ConsultFindConsultant : AppCompatActivity() {

    private lateinit var binding: ActivityConsultFindConsultantBinding
    private lateinit var viewModel: ConsultantFindViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultFindConsultantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Temukan Pakar Terbaikmu"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ConsultantFindViewModel::class.java]

        binding.rvConsultantFind.layoutManager = LinearLayoutManager(this)
        val adapter = ConsultantFindAdapter()
        adapter.notifyDataSetChanged()
        binding.rvConsultantFind.adapter = adapter


        binding.progressBar.visibility = View.VISIBLE
        viewModel.setUser()
        viewModel.getUser().observe(this, {  consultantVerifyList ->

            if(consultantVerifyList.size > 0) {
                binding.noData.visibility = View.GONE
                adapter.setData(consultantVerifyList)
            } else {
                binding.noData.visibility = View.VISIBLE
            }
            binding.progressBar.visibility = View.GONE
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}