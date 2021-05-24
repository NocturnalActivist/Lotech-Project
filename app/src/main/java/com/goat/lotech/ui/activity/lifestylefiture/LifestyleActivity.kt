package com.goat.lotech.ui.activity.lifestylefiture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.goat.lotech.R
import com.goat.lotech.databinding.ActivityLifestyleBinding

class LifestyleActivity : AppCompatActivity() {

    private lateinit var activityLifestyleBinding: ActivityLifestyleBinding
    private lateinit var lifestyleAdapter: LifestyleAdapter
    private lateinit var viewModel: LifestyleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lifestyle)

        supportActionBar?.title = "Lifestyle Information"

        activityLifestyleBinding = ActivityLifestyleBinding.inflate(layoutInflater)
        setContentView(activityLifestyleBinding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[LifestyleViewModel::class.java]
        val lifestyle = viewModel.getLifestyle()

        lifestyleAdapter = LifestyleAdapter()
        lifestyleAdapter.setLifestyle(lifestyle)
        lifestyleAdapter.notifyDataSetChanged()

        showRecyleview()

    }

    private fun showRecyleview() {
        activityLifestyleBinding.rvLifestyle.layoutManager = LinearLayoutManager(this)
        activityLifestyleBinding.rvLifestyle.adapter = lifestyleAdapter

        showLoading(false)

    }

    private fun showLoading(state: Boolean) {
        if (state)activityLifestyleBinding.progressBar.visibility = View.VISIBLE
        else activityLifestyleBinding.progressBar.visibility= View.GONE
    }
}