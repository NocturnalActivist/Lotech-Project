package com.goat.lotech.ui.activity.marketplace.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.goat.lotech.R
import com.goat.lotech.databinding.ActivityMarketplaceMainBinding

class MarketplaceMainActivity : AppCompatActivity(), MarketplaceSortAdapter.OnItemClickCallback {

    private lateinit var binding: ActivityMarketplaceMainBinding
    private lateinit var viewModel: MarketplaceMainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketplaceMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        loadButtonSort()
        loadProduct("all")

        supportActionBar?.title = "Lotech Store"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MarketplaceMainViewModel::class.java]
    }

    private fun loadProduct(option: String) {
        binding.rvMarketplace.layoutManager = GridLayoutManager(this, 2)
        val adapter = MarketplaceMainAdapter()
        adapter.notifyDataSetChanged()
        binding.rvMarketplace.adapter = adapter

        binding.progressBar.visibility = View.VISIBLE
        viewModel.setAllItems(option)
        viewModel.getProductList().observe(this, { product ->
            if (product.size > 0) {
                binding.noData.visibility = View.GONE
                binding.rvMarketplace.visibility = View.VISIBLE
                adapter.setData(product)
            } else {
                binding.noData.visibility = View.VISIBLE
                binding.rvMarketplace.visibility = View.GONE
            }
            binding.progressBar.visibility = View.GONE
        })
    }

    private fun loadButtonSort() {

        val sortedBy = listOf("Makanan", "Minuman", "Vitamin", "Obat")

        val data: MutableList<MarketplaceSortModel> = ArrayList()
        for (i in 0..3) {
            data.add(MarketplaceSortModel(sortedBy[i]))
        }

        val adapter = MarketplaceSortAdapter(data)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSort.layoutManager = layoutManager
        binding.rvSort.setHasFixedSize(true)
        binding.rvSort.adapter = adapter

        adapter.setOnItemClickCallback(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun clickFavoriteButton(title: String?) {
        when (title) {
            "Makanan" -> {
                loadProduct("Makanan")
            }
            "Minuman" -> {
                loadProduct("Minuman")
            }
            "Vitamin" -> {
                loadProduct("Vitamin")
            }
            "Obat" -> {
                loadProduct("Obat")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_marketplace, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.bukatoko) {
            Toast.makeText(this, "Fitur Buka Toko sedang dikembangkan", Toast.LENGTH_SHORT).show()
        } else if (item.itemId == R.id.tokosaya){
            Toast.makeText(this, "Fitur Toko Saya sedang dikembangkan", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}