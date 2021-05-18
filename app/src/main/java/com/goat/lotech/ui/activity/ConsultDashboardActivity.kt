package com.goat.lotech.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.goat.lotech.R
import com.goat.lotech.databinding.ActivityConsultDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ConsultDashboardActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityConsultDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Konsultasi"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.progressBar.visibility = View.VISIBLE
        binding.view.visibility = View.GONE
        binding.view2.visibility = View.GONE
        binding.view3.visibility = View.GONE
        checkUserRoleForVerifyUser()

        binding.view.setOnClickListener(this)
        binding.view2.setOnClickListener(this)
        binding.view3.setOnClickListener(this)

    }

    private fun checkUserRoleForVerifyUser() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val docRef = uid?.let { Firebase.firestore.collection("users").document(it) }
        docRef?.get()
            ?.addOnSuccessListener { result ->
                  if(result["role"].toString() == "super") {
                      binding.progressBar.visibility = View.GONE
                      binding.view.visibility = View.VISIBLE
                      binding.view2.visibility = View.VISIBLE
                      binding.view3.visibility = View.VISIBLE
                  } else {
                      binding.progressBar.visibility = View.GONE
                      binding.view.visibility = View.VISIBLE
                      binding.view2.visibility = View.VISIBLE
                      binding.imageView3.visibility = View.GONE
                      binding.textView5.visibility = View.GONE
                      binding.textView7.visibility = View.GONE
                  }
            }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.view -> {
                startActivity(Intent(this, ConsultFindConsultant::class.java))
            }
            R.id.view2 -> {
                checkIfThisUserAlreadyRegistered()
            }
            R.id.view3 -> {
                startActivity(Intent(this, ConsultVerifyUserActivity::class.java))
            }
        }
    }

    private fun checkIfThisUserAlreadyRegistered() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val docRef = uid?.let { Firebase.firestore.collection("consultant").document(it) }
        docRef?.get()
            ?.addOnSuccessListener { result ->
               if(!result.exists()) {
                   startActivity(Intent(this, ConsultAddExpertActivity::class.java))
               } else if (result["status"] == "waiting") {
                   val builder = AlertDialog.Builder(this)
                   builder.setTitle("Silahkan menunggu yah :)")
                   builder.setMessage("Anda telah berhasil terdaftar untuk menjadi Pakar Konsultasi\n\nLotech akan melakukan verifikasi terhadap data diri yang anda kirimkan\n\nMohon menunggu, Terima kasih :)")
                   builder.setPositiveButton("YES") { dialog, _ ->
                       dialog.dismiss()
                   }.show()
               } else if(result["status"] == "pakar") {
                   val builder = AlertDialog.Builder(this)
                   builder.setTitle("Anda Sudah Terdaftar")
                   builder.setMessage("Mendaftar menjadi pakar konsultasi hanya dapat dilakukan sekali, kecuali izin praktik anda kami cabut, anda kemungkinan bisa mendaftar ulang")
                   builder.setPositiveButton("YES") { dialog, _ ->
                       dialog.dismiss()
                   }.show()
               }
            }
    }
}