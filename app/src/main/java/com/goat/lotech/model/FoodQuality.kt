package com.goat.lotech.model

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.goat.lotech.ui.activity.FoodQualityActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FoodQuality {

    private val TAG = FoodQuality::class.java.simpleName
    var result: Boolean? = false

    var gender: String? = null
    var birthDate: Int? = 0

    @SuppressLint("SetTextI18n")
    fun loadUserData(
        uid: String,
        tvName: TextView,
        tvWeight: TextView,
        tvHeight: TextView,
        content: TextView,
        progressBar: ProgressBar,
        tvHasil: TextView,
        context: FoodQualityActivity
    ) {
        Firebase.firestore
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {

                val weight = it["weightBody"].toString()
                val height = it["heightBody"].toString()
                gender = it["gender"].toString()
                birthDate = 2021 - it["birthDate"].toString().substring(0,4).toInt()


                tvName.text = it["name"].toString()
                tvWeight.text = weight
                tvHeight.text = height

                tvHasil.text = "Kekurangan kalori 100%"

                if(gender == "Laki-laki") {
                    val bmr = 66.5 + (13.7 * weight.toInt()) + (5 * height.toInt()) - (6.8 * birthDate!!)
                    content.text = "Berdasarkan berat badan, tinggi badan, dan umur, kamu disarankan untuk mengkonsumsi kalori sebesar $bmr kalori/hari dan hari ini kamu mengkonsumsi 0 Kalori, sehingga:"
                } else {
                    val bmr = 66.5 + (9.6 * weight.toInt()) + (1.8 * height.toInt()) - (4.7 * birthDate!!)
                    content.text = "Berdasarkan berat badan, tinggi badan, dan umur, kamu disarankan untuk mengkonsumsi kalori sebesar $bmr kalori/hari dan hari ini kamu mengkonsumsi 0 Kalori, sehingga:"
                }

                progressBar.visibility = View.GONE
            }
            .addOnFailureListener {
                Log.e(TAG, "error: $it")
                Toast.makeText(context, "Gagal Mengambil data", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
    }

    fun getFood(foodName: String, context: FoodQualityActivity, listItem: ArrayList<FoodModel>) {

        Firebase.firestore
            .collection("dataset")
            .document(foodName)
            .get()
            .addOnSuccessListener {

                if(it.exists()) {
                    val model  = FoodModel()

                    model.foodName = foodName
                    model.calories = "Energi: ${it["energi(kal)"].toString()} kalori"
                    model.protein = "Protein: ${ it["protein(g)"].toString()} gram"
                    model.fat = "Lemak: ${it["lemak(g)"].toString()} gram"
                    model.karbohidrat = "Karbohidrat: ${it["karbohidrat(g)"].toString()} gram"
                    model.serat = "Serat: ${it["serat(g)"].toString()} gram"
                    model.vitaminc = "Vitamin C: ${it["vitaminc(mg)"].toString()} mg"

                    if(listItem.size > 0) {
                        model.calTot = listItem[listItem.size-1].calTot?.plus(it["energi(kal)"].toString().toDouble())
                        model.protTot = listItem[listItem.size-1].protTot?.plus(it["protein(g)"].toString().toDouble())
                        model.fatTor = listItem[listItem.size-1].fatTor?.plus(it["lemak(g)"].toString().toDouble())
                        model.carTor = listItem[listItem.size-1].carTor?.plus(it["karbohidrat(g)"].toString().toDouble())
                        model.seratTor = listItem[listItem.size-1].seratTor?.plus(it["serat(g)"].toString().toDouble())
                        model.vitcTot = listItem[listItem.size-1].vitcTot?.plus(it["vitaminc(mg)"].toString().toDouble())
                    } else {
                        model.calTot = it["energi(kal)"].toString().toDouble()
                        model.protTot = it["protein(g)"].toString().toDouble()
                        model.fatTor = it["lemak(g)"].toString().toDouble()
                        model.carTor = it["karbohidrat(g)"].toString().toDouble()
                        model.seratTor = it["serat(g)"].toString().toDouble()
                        model.vitcTot = it["vitaminc(mg)"].toString().toDouble()
                    }
                    listItem.add( model)
                    result = true
                } else {
                    Log.e(TAG, "error: $it")
                    Toast.makeText(context, "Makanan belum tersedia", Toast.LENGTH_SHORT).show()
                    result = false
                }


            }
            .addOnFailureListener {
                Log.e(TAG, "error: $it")
                Toast.makeText(context, "Gagal Mengambil data", Toast.LENGTH_SHORT).show()
                result = false
            }
    }

}