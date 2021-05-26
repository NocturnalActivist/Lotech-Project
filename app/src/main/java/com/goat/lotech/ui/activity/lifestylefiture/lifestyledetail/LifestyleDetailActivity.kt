package com.goat.lotech.ui.activity.lifestylefiture.lifestyledetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient


import com.goat.lotech.databinding.ActivityLifestyleDetailBinding

class LifestyleDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LIFESTYLE = "extra_lifestyle"
    }

    private lateinit var lifestyleDetailBinding: ActivityLifestyleDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifestyleDetailBinding = ActivityLifestyleDetailBinding.inflate(layoutInflater)
        setContentView(lifestyleDetailBinding.root)

        supportActionBar?.title = "Lifestyle News"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val url: String = intent.getStringExtra(EXTRA_LIFESTYLE).toString()

        lifestyleDetailBinding.progressBar.visibility = View.VISIBLE

        lifestyleDetailBinding.webView.settings.javaScriptEnabled

        lifestyleDetailBinding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }
        }
        lifestyleDetailBinding.webView.loadUrl(url)
        lifestyleDetailBinding.progressBar.visibility = View.GONE


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}