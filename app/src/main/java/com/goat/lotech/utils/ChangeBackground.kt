package com.goat.lotech.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.goat.lotech.R
import java.util.*

object ChangeBackground {

    fun changeBackgroundByTime(
        activity: FragmentActivity?,
        background: ImageView,
        greeting: TextView
    ) {
        val calendar = Calendar.getInstance()

        when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 0..10 -> {
                activity?.let {

                    greeting.text = "Selamat Pagi"

                    Glide.with(it)
                        .load("https://images.unsplash.com/photo-1546637703-5fc4a4bf42ea?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80")
                        .placeholder(R.color.pink_primary)
                        .error(R.color.pink_primary)
                        .into(background)
                }
            }
            in 11..14 -> {

                greeting.text = "Selamat Siang"

                activity?.let {
                    Glide.with(it)
                        .load("https://images.unsplash.com/photo-1476224203421-9ac39bcb3327?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80")
                        .placeholder(R.color.pink_primary)
                        .error(R.color.pink_primary)
                        .into(background)
                }
            }
            in 15..18 -> {

                greeting.text = "Selamat Sore"

                activity?.let {
                    Glide.with(it)
                        .load("https://images.unsplash.com/photo-1517833969405-d4a24c2c8280?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80")
                        .placeholder(R.color.pink_primary)
                        .error(R.color.pink_primary)
                        .into(background)
                }
            }
            else -> {

                greeting.text = "Selamat Malam"

                activity?.let {
                    Glide.with(it)
                        .load("https://images.unsplash.com/photo-1491219662911-6300ce099062?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=977&q=80")
                        .placeholder(R.color.pink_primary)
                        .error(R.color.pink_primary)
                        .into(background)
                }
            }
        }
    }

}