package com.goat.lotech.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.goat.lotech.R
import com.goat.lotech.databinding.ActivityMainBinding
import com.goat.lotech.ui.fragment.ChatFragment
import com.goat.lotech.ui.fragment.HomeFragment
import com.goat.lotech.ui.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener {
            var selectedFragment: Fragment = HomeFragment()

            when (it.itemId) {
                R.id.navigation_home -> {
                    navView.menu.findItem(R.id.navigation_home).isEnabled = false
                    navView.menu.findItem(R.id.navigation_chat).isEnabled = true
                    navView.menu.findItem(R.id.navigation_profile).isEnabled = true
                    selectedFragment = HomeFragment()
                }
                R.id.navigation_chat -> {
                    navView.menu.findItem(R.id.navigation_chat).isEnabled = false
                    navView.menu.findItem(R.id.navigation_profile).isEnabled = true
                    navView.menu.findItem(R.id.navigation_home).isEnabled = true
                    selectedFragment = ChatFragment()

                }
                R.id.navigation_profile -> {
                    navView.menu.findItem(R.id.navigation_profile).isEnabled = false
                    navView.menu.findItem(R.id.navigation_home).isEnabled = true
                    navView.menu.findItem(R.id.navigation_chat).isEnabled = true
                    selectedFragment = ProfileFragment()
                }
                else -> {
                    navView.menu.findItem(R.id.navigation_home).isEnabled = false
                }
            }
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, selectedFragment)
            transaction.commit()
            return@setOnNavigationItemSelectedListener true
        }
    }
}