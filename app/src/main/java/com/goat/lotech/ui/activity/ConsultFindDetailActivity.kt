package com.goat.lotech.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ConsultFindDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsultantFindDetailBinding
    private var isFavorite = false
    private var like = 0

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
        like = user.like!!
        val price = user.price

        binding.name.text = name
        binding.description.text = desc
        binding.sertifikatKeahlian.text = sertifikatKeahlian
        binding.noHp.text = noHp
        binding.liked.text = "$like Orang Merekomendasikan $name"
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
        checkIfRecommendedUser(name, uid)

        // update tarif konsultasi & hide chat button
        if (FirebaseAuth.getInstance().currentUser?.uid == uid) {

            binding.chatPakar.visibility = View.INVISIBLE

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
            saveData(name, uid)
        }

        // chat pakar
        binding.chatPakar.setOnClickListener {
            getMyNameAndMySelfPhotoBefore(name, selfPhoto, uid)
        }

    }

    private fun getMyNameAndMySelfPhotoBefore(name: String?, selfPhoto: String?, uid: String?) {
        FirebaseAuth.getInstance().currentUser?.uid?.let {
            Firebase.firestore
                .collection("users")
                .document(it)
                .get()
                .addOnSuccessListener { result ->

                    val intent = Intent(this, ChatUserActivity::class.java)
                    intent.putExtra(ChatUserActivity.NAME, name)
                    intent.putExtra(ChatUserActivity.SELF_PHOTO, selfPhoto)
                    intent.putExtra(ChatUserActivity.UID, uid)
                    intent.putExtra(ChatUserActivity.MY_NAME, result["name"].toString())
                    intent.putExtra(ChatUserActivity.MY_SELF_PHOTO, result["image"].toString())

                    startActivity(intent)

                }
                .addOnFailureListener { err ->
                    Log.e("TAG", err.message.toString())
                }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun saveData(name: String?, uid: String?) {
        checkIfRecommendedUser(name, uid)
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        val myUid = FirebaseAuth.getInstance().currentUser?.uid

        if(isFavorite) {
            with(sharedPref.edit()) {
                putBoolean("status_${myUid}_${uid}", false)
                commit()
            }
            binding.merekomendasikan.text = "Saya ingin merekomendasikan $name"
            binding.merekomendasikan.backgroundTintList =
                ContextCompat.getColorStateList(applicationContext,R.color.pink_primary)


            like -= 1
            AddConsultant.likedOrNot(like, uid)
            binding.liked.text = "$like Orang merekomendasikan $name"
        } else {
            with(sharedPref.edit()) {
                putBoolean("status_${myUid}_${uid}", true)
                commit()
            }
            binding.merekomendasikan.text = "Saya tidak ingin merekomendasikan $name"
            binding.merekomendasikan.backgroundTintList =
                ContextCompat.getColorStateList(applicationContext,R.color.pink_Light)

            like += 1
            AddConsultant.likedOrNot(like, uid)
            binding.liked.text = "$like Orang merekomendasikan $name"
        }

        Log.d("TAG", "status_${myUid}_${uid} : " + isFavorite)

    }

    @SuppressLint("SetTextI18n")
    private fun checkIfRecommendedUser(name: String?, uid: String?) {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        val myUid = FirebaseAuth.getInstance().currentUser?.uid
        val value = sharedPref.getBoolean("status_${myUid}_${uid}", false)

        Log.d("TAG", "status_${myUid}_${uid} : " + value.toString())

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