package com.goat.lotech.ui.activity.lifestylefiture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.goat.lotech.R
import com.goat.lotech.databinding.ActivityLifestyleBinding

class LifestyleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLifestyleBinding
    private lateinit var viewModel: LifestyleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lifestyle)

        supportActionBar?.title = "Lifestyle Information"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityLifestyleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[LifestyleViewModel::class.java]

        binding.rvLifestyle.layoutManager = LinearLayoutManager(this)
        val adapter = LifestyleAdapter()
        binding.rvLifestyle.adapter = adapter


        binding.progressBar.visibility = View.VISIBLE
        viewModel.setLifestyle()
        viewModel.getLifestyle().observe(this, {

            if(it != null) {
                adapter.listData = it.articles.toMutableList()
                adapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
            } else {
                Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        })
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}