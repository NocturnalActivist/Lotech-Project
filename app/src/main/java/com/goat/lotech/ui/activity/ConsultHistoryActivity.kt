package com.goat.lotech.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.goat.lotech.databinding.ActivityConsultHistoryBinding
import com.goat.lotech.viewmodel.adapter.SectionPagerAdapter


class ConsultHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsultHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Riwayat Konsultasi"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

       viewPagerConfig()


    }

    private fun viewPagerConfig() {
        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = sectionPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)
        supportActionBar?.elevation = 0f
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


}