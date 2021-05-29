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
        binding.view69.visibility = View.GONE
        binding.textView2.visibility = View.GONE
        binding.textView3.visibility = View.GONE
        binding.textView4.visibility = View.GONE
        binding.textView5.visibility = View.GONE
        binding.textView6.visibility = View.GONE
        binding.textView7.visibility = View.GONE
        binding.textView8.visibility = View.GONE
        binding.textView9.visibility = View.GONE
        binding.imageView.visibility = View.GONE
        binding.imageView2.visibility = View.GONE
        binding.imageView3.visibility = View.GONE
        binding.imageView4.visibility = View.GONE
        checkUserRoleForVerifyUser()

        binding.view.setOnClickListener(this)
        binding.view2.setOnClickListener(this)
        binding.view3.setOnClickListener(this)
        binding.view69.setOnClickListener(this)

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
                      binding.view69.visibility = View.VISIBLE

                      binding.textView2.visibility = View.VISIBLE
                      binding.textView3.visibility = View.VISIBLE
                      binding.textView4.visibility = View.VISIBLE
                      binding.textView5.visibility = View.VISIBLE
                      binding.textView6.visibility = View.VISIBLE
                      binding.textView7.visibility = View.VISIBLE
                      binding.textView8.visibility = View.VISIBLE
                      binding.textView9.visibility = View.VISIBLE

                      binding.imageView.visibility = View.VISIBLE
                      binding.imageView2.visibility = View.VISIBLE
                      binding.imageView3.visibility = View.VISIBLE
                      binding.imageView4.visibility = View.VISIBLE

                  } else {
                      binding.progressBar.visibility = View.GONE
                      binding.view.visibility = View.VISIBLE
                      binding.view2.visibility = View.VISIBLE
                      binding.view69.visibility = View.VISIBLE

                      binding.imageView3.visibility = View.GONE
                      binding.imageView.visibility = View.VISIBLE
                      binding.imageView2.visibility = View.VISIBLE
                      binding.imageView4.visibility = View.VISIBLE

                      binding.textView2.visibility = View.VISIBLE
                      binding.textView3.visibility = View.VISIBLE
                      binding.textView4.visibility = View.VISIBLE
                      binding.textView6.visibility = View.VISIBLE
                      binding.textView8.visibility = View.VISIBLE
                      binding.textView9.visibility = View.VISIBLE
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
                showAlertDialogToVerify()
            }
            R.id.view69 -> {
                startActivity(Intent(this, ConsultHistoryActivity::class.java))
            }
        }
    }

    private fun showAlertDialogToVerify() {
        val options = arrayOf(
            "Verifikasi Berkasi Calon Pakar",
            "Verifikasi Transaksi Pengguna",
        )

        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Pilihan Verifikasi")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> {
                    startActivity(Intent(this, ConsultVerifyUserActivity::class.java))
                    dialog.dismiss()
                }
                1 -> {
                    startActivity(Intent(this, ConsultHistoryActivity::class.java))
                    dialog.dismiss()
                }
            }
        }
        builder.create().show()
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