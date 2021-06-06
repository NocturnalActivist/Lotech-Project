package com.goat.lotech.ui.activity.marketplace.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.goat.lotech.R
import com.goat.lotech.databinding.ActivityMarketplaceDetailBinding
import com.goat.lotech.ui.activity.marketplace.main.MarketplaceMainModel

class MarketplaceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMarketplaceDetailBinding

    companion object {
        const val EXTRA_ITEM = "item"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketplaceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val product = intent.getParcelableExtra<MarketplaceMainModel>(EXTRA_ITEM) as MarketplaceMainModel

        supportActionBar?.title = product.productName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.productName.text = product.productName
        binding.productDescription.text = product.productDescription
        binding.price.text = "Rp. " + product.price.toString()

        Glide.with(this)
            .load(product.productDp)
            .placeholder(R.drawable.ic_baseline_refresh_24)
            .error(R.drawable.ic_baseline_refresh_24)
            .into(binding.productDp)

        binding.addToBucket.setOnClickListener {
            Toast.makeText(this, "Fitur Tambahkan ke Keranjang sedang dikembangkan", Toast.LENGTH_SHORT).show()
        }

        binding.comment.setOnClickListener {
            Toast.makeText(this, "Fitur Komentar sedang dikembangkan", Toast.LENGTH_SHORT).show()
        }

        binding.share.setOnClickListener {
            Toast.makeText(this, "Fitur Bagikan Produk sedang dikembangkan", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}