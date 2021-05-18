package com.goat.lotech.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.goat.lotech.databinding.ActivityConsultVerifyUserBinding
import com.goat.lotech.viewmodel.adapter.ConsultantVerifyAdapter
import com.goat.lotech.viewmodel.viewmodel.ConsultantVerifyUserViewModel

class ConsultVerifyUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsultVerifyUserBinding
    private lateinit var viewModel: ConsultantVerifyUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultVerifyUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Resume Pendaftar"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ConsultantVerifyUserViewModel::class.java]

        binding.rvConsultantVerify.layoutManager = LinearLayoutManager(this)
        val adapter = ConsultantVerifyAdapter()
        adapter.notifyDataSetChanged()
        binding.rvConsultantVerify.adapter = adapter


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