package com.goat.lotech.ui.activity.lifestylefiture.lifestyledetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.goat.lotech.R
import com.goat.lotech.databinding.ActivityLifestyleDetailBinding
import com.goat.lotech.model.LifestyleModel
import com.goat.lotech.ui.activity.lifestylefiture.LifestyleViewModel

class LifestyleDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LIFESTYLE = "extra_lifestyle"
    }

    private lateinit var lifestyleDetailBinding: ActivityLifestyleDetailBinding
    private lateinit var viewModel: LifestyleDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifestyleDetailBinding = ActivityLifestyleDetailBinding.inflate(layoutInflater)
        setContentView(lifestyleDetailBinding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[LifestyleDetailViewModel::class.java]

        val extras = intent.extras
        if (extras != null) {
            val lifestyleId = extras.getString(EXTRA_LIFESTYLE)
            if (lifestyleId != null) {
                viewModel.setSelectedLifestyle(lifestyleId)
                populateLifestye(viewModel.getLifestyle())

            }
        }
    }

    private fun populateLifestye(lifestyleModel: LifestyleModel) {
        lifestyleDetailBinding.titleDetailLifestyle.text = lifestyleModel.title
        lifestyleDetailBinding.descriptionDetailLifestyle.text = lifestyleModel.information

        Glide.with(this)
            .load(lifestyleModel.image)
            .into(lifestyleDetailBinding.imageDetailLifestyle)

    }
}