package com.goat.lotech.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.goat.lotech.R
import com.goat.lotech.databinding.ActivityConsultantFindDetailBinding
import com.goat.lotech.model.AddConsultant
import com.goat.lotech.model.ConsultantVerifyModel
import com.google.firebase.auth.FirebaseAuth


class ConsultantFindDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsultantFindDetailBinding
    private var isFavorite = false

    companion object {
        const val EXTRA_USER = "user"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultantFindDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Pakar Pilihanmu"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.progressBar.visibility = View.VISIBLE
        // Load Data Pengguna
        val user =
            intent.getParcelableExtra<ConsultantVerifyModel>(EXTRA_USER) as ConsultantVerifyModel
        val uid = user.uid
        val name = user.name
        val desc = user.description
        val noHp = user.noHp
        val sertifikatKeahlian = user.sertifikatKeahlian
        val selfPhoto = user.selfPhoto
        val sertifikat = user.sertifikat
        val liked = user.like
        val price = user.price

        binding.name.text = name
        binding.description.text = desc
        binding.sertifikatKeahlian.text = sertifikatKeahlian
        binding.noHp.text = noHp
        binding.liked.text = liked.toString() + " Orang Merekomendasikan $name"
        binding.price.text = "Rp. $price"

        Glide.with(this)
            .load(selfPhoto)
            .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_face_24))
            .error(R.drawable.ic_baseline_face_24)
            .into(binding.selfPhoto)

        Glide.with(this)
            .load(sertifikat)
            .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_face_24))
            .error(R.drawable.ic_baseline_face_24)
            .into(binding.keahlian)
        binding.progressBar.visibility = View.GONE


        // Cek Apakah Saya Merekomendasikan pakar ini?
        checkIfRecommendedUser(name)


        // update tarif konsultasi
        if (FirebaseAuth.getInstance().currentUser?.uid == uid) {
            binding.changePrice.visibility = View.VISIBLE
            binding.changePrice.setOnClickListener {
                createAlertDialogForChangePrice()
            }
        } else {
            binding.changePrice.visibility = View.GONE
        }


        // telpon pakar
        binding.telponPakar.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", noHp, null)))
        }

        // saya merekomendasikan pakar ini
        binding.merekomendasikan.setOnClickListener {
            saveData(name, liked, uid)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun saveData(name: String?, liked: Int?, uid: String?) {
        checkIfRecommendedUser(name)
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        if(isFavorite) {
            with(sharedPref.edit()) {
                putBoolean("status", false)
                commit()
            }
            binding.merekomendasikan.text = "Saya ingin merekomendasikan $name"
            binding.merekomendasikan.backgroundTintList =
                ContextCompat.getColorStateList(applicationContext,R.color.pink_primary)

            val notLiked = liked?.minus(1)
            AddConsultant.likedOrNot(notLiked, uid)
            binding.liked.text = liked?.minus(1).toString() + " Orang merekomendasikan $name"
        } else {
            with(sharedPref.edit()) {
                putBoolean("status", true)
                commit()
            }
            binding.merekomendasikan.text = "Saya tidak ingin merekomendasikan $name"
            binding.merekomendasikan.backgroundTintList =
                ContextCompat.getColorStateList(applicationContext,R.color.pink_Light)

            val like = liked?.plus(1)
            AddConsultant.likedOrNot(like, uid)
            binding.liked.text = liked?.plus(1).toString() + " Orang merekomendasikan $name"
        }

    }

    @SuppressLint("SetTextI18n")
    private fun checkIfRecommendedUser(name: String?) {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        val value = sharedPref.getBoolean("status", false)

        if(value) {
            isFavorite = true
            binding.merekomendasikan.text = "Saya tidak ingin merekomendasikan $name"
            binding.merekomendasikan.backgroundTintList =
                ContextCompat.getColorStateList(applicationContext,R.color.pink_Light)
        } else {
            isFavorite = false
            binding.merekomendasikan.text = "Saya ingin merekomendasikan $name"
            binding.merekomendasikan.backgroundTintList =
                ContextCompat.getColorStateList(applicationContext,R.color.pink_primary)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun createAlertDialogForChangePrice() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Perbarui Tarif Konsultasi")
        val updatedPrice = EditText(this)
        updatedPrice.setRawInputType(InputType.TYPE_CLASS_NUMBER)
        builder.setView(updatedPrice)

        builder.setPositiveButton("PERBARUI") { dialog, _ ->
            AddConsultant.changePrice(updatedPrice.text.toString(), this)
            binding.price.text = "Rp. ${updatedPrice.text}"
            dialog.dismiss()

        }
        builder.setNegativeButton("TIDAK") { dialog, _ ->
            dialog.dismiss()
        }.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}