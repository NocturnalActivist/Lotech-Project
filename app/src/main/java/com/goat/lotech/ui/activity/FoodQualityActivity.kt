package com.goat.lotech.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.goat.lotech.databinding.ActivityFoodQualityBinding
import com.goat.lotech.model.FoodModel
import com.goat.lotech.model.FoodQuality
import com.goat.lotech.viewmodel.adapter.FoodAdapter
import com.google.firebase.auth.FirebaseAuth

class FoodQualityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodQualityBinding
    private val listItem = ArrayList<FoodModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodQualityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Cek Kualitas Makanan Harianmu"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        binding.progressBar.visibility = View.VISIBLE
        FoodQuality.loadUserData(
            uid,
            binding.tvName,
            binding.tvWeight,
            binding.tvHeight,
            binding.textView11,
            binding.progressBar,
            binding.tvHasil,
            this
        )

        binding.btnFood.setOnClickListener {
            if(binding.namaMakanan.text.toString().trim().isNotEmpty()) {
                getFoodDatasetFromDB()
            } else {
                Toast.makeText(this, "Kolom tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFoodDatasetFromDB() {
        val foodName = binding.namaMakanan.text.toString().trim()
        binding.progressBar.visibility = View.VISIBLE


        FoodQuality.getFood(foodName, this, listItem)

        // todo 3333 itu delay dummy, soalnya saya blm tau cara nunggu proses diatas selesai duluan, makanya saya buat dummy delay
        Handler(Looper.getMainLooper()).postDelayed({
            if(FoodQuality.result == true) {
                setFood()
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }, 3333)

    }

    @SuppressLint("SetTextI18n")
    fun setFood() {
        Log.e("TAG", listItem.size.toString())


        val linearLayout = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = linearLayout
        val adapter = FoodAdapter(listItem)
        adapter.notifyDataSetChanged()
        binding.recyclerView.adapter = adapter

        binding.tvEnergi.text = "${String.format("%.2f", listItem[listItem.size-1].calTot)} kal"
        binding.tvProtein.text = "${String.format("%.2f", listItem[listItem.size-1].protTot)} gr"
        binding.tvLemak.text = "${String.format("%.2f", listItem[listItem.size-1].fatTor)} gr"
        binding.tvCarbohidrat.text = "${String.format("%.2f", listItem[listItem.size-1].carTor)} gr"
        binding.tvSerat.text = "${String.format("%.2f", listItem[listItem.size-1].seratTor)} gr"
        binding.tvVitaminC.text = "${String.format("%.2f", listItem[listItem.size-1].vitcTot)} mg"


        if(FoodQuality.gender == "Laki-laki") {
            val bmr = 66.5 + (13.7 * binding.tvWeight.text.toString().toDouble()) + (5 * binding.tvHeight.text.toString().toDouble()) - (6.8 * FoodQuality.birthDate!!)
            binding.textView11.text = "Berdasarkan berat badan, tinggi badan, dan umur, kamu disarankan untuk mengkonsumsi kalori sebesar $bmr kalori/hari dan hari ini kamu mengkonsumsi total ${listItem[listItem.size-1].calTot.toString()} Kalori, sehingga:"
            val percentage = (listItem[listItem.size-1].calTot?.div(bmr))?.times(100.0)
            if(percentage?.toInt()!! < 100) {
                binding.tvHasil.text = "Hari ini anda telah mencukupi asupan kalori sebanyak ${String.format("%.2f", percentage)} %\nSebaiknya anda mencukupi kalori harian anda agar pola hidup anda menjadi lebih sehat."
            } else {
                binding.tvHasil.text = "Ups anda kelebihan kalori\nSebaiknya anda mengurangi porsi makan anda sehingga tidak melebihi kalori harian anda, agar pola hidup anda menjadi lebih sehat."
            }
        } else {
            val bmr = 66.5 + (9.6 * binding.tvWeight.text.toString().toDouble()) + (1.8 * binding.tvHeight.text.toString().toDouble()) - (4.7 * FoodQuality.birthDate!!)
            binding.textView11.text = "Berdasarkan berat badan, tinggi badan, dan umur, kamu disarankan untuk mengkonsumsi kalori sebesar $bmr kalori/hari dan hari ini kamu mengkonsumsi ${listItem[listItem.size-1].calTot.toString()} Kalori, sehingga:"
            val percentage = (listItem[listItem.size-1].calTot?.div(bmr))?.times(100.0)
            if(percentage?.toInt()!! < 100) {
                binding.tvHasil.text = "Hari ini anda telah mencukupi asupan kalori sebanyak ${String.format("%.2f", percentage)} %\nSebaiknya anda mencukupi kalori harian anda agar pola hidup anda menjadi lebih sehat."
            } else {
                binding.tvHasil.text = "Ups anda kelebihan kalori\nSebaiknya anda mengurangi porsi makan anda sehingga tidak melebihi kalori harian anda, agar pola hidup anda menjadi lebih sehat."
            }
        }

        binding.progressBar.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}