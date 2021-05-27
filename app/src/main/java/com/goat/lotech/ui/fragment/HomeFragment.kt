package com.goat.lotech.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.goat.lotech.R
import com.goat.lotech.databinding.ActivityMainBinding
import com.goat.lotech.databinding.FragmentHomeBinding
import com.goat.lotech.ml.MLMainActivity
import com.goat.lotech.model.Home
import com.goat.lotech.ui.activity.ConsultDashboardActivity
import com.goat.lotech.ui.activity.EditProfileActivity
import com.goat.lotech.ui.activity.FoodQualityActivity

import com.goat.lotech.utils.ChangeBackground

import com.goat.lotech.ui.activity.lifestylefiture.LifestyleActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        // set background
        ChangeBackground.changeBackgroundByTime(activity, binding.background, binding.textHomepage)

        //Load userData
        Home.loadUserData(binding.name, binding.imageHomepage, activity)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.consult.setOnClickListener {
            startActivity(Intent(activity, ConsultDashboardActivity::class.java))
        }

        binding.cekKalori.setOnClickListener {
            startActivity(Intent(activity, MLMainActivity::class.java))
        }

        binding.lifestyle.setOnClickListener{
            startActivity(Intent(activity, LifestyleActivity::class.java))
        }

        binding.foodQuality.setOnClickListener{
            startActivity(Intent(activity, FoodQualityActivity::class.java ))
        }


    }
}