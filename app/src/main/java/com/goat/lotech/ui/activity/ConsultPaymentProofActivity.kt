package com.goat.lotech.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.goat.lotech.R
import com.goat.lotech.databinding.ActivityConsultPaymentProofBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ConsultPaymentProofActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsultPaymentProofBinding

    companion object {
        const val EXTRA_DOCUMENT = "doc"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultPaymentProofBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Bukti Pembayaran Pengguna"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadUserPaymentProof()
    }

    private fun loadUserPaymentProof() {


        val doc = intent.getStringExtra(EXTRA_DOCUMENT).toString()

        binding.progressBar.visibility = View.VISIBLE
        Firebase.firestore
            .collection("consult_history")
            .document(doc)
            .get()
            .addOnSuccessListener {
                Glide.with(this)
                    .load(it["bukti"].toString())
                    .placeholder(R.drawable.ic_baseline_face_24)
                    .error(R.drawable.ic_baseline_face_24)
                    .into(binding.proof)
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Ups, ada kendala nih", Toast.LENGTH_SHORT).show()
                Log.e("Error: ", it.toString())
            }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}