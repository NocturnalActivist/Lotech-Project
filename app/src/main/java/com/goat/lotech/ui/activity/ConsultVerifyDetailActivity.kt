package com.goat.lotech.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.goat.lotech.R
import com.goat.lotech.databinding.ActivityConsultVerifyDetailBinding
import com.goat.lotech.model.AddConsultant
import com.goat.lotech.model.ConsultantVerifyModel

class ConsultVerifyDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsultVerifyDetailBinding

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultVerifyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Resume Pendaftar"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.progressBar.visibility = View.VISIBLE
        val user = intent.getParcelableExtra<ConsultantVerifyModel>(EXTRA_USER) as ConsultantVerifyModel
        val uid = user.uid
        val name = user.name
        val desc = user.description
        val noHp = user.noHp
        val sertifikatKeahlian = user.sertifikatKeahlian
        val selfPhoto = user.selfPhoto
        val ktp = user.ktp
        val sertifikat = user.sertifikat

        binding.name.text = name
        binding.description.text = desc
        binding.sertifikatKeahlian.text = sertifikatKeahlian
        binding.noHp.text = noHp

        Glide.with(this)
            .load(selfPhoto)
            .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_face_24))
            .error(R.drawable.ic_baseline_face_24)
            .into(binding.selfPhoto)


        Glide.with(this)
            .load(ktp)
            .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_face_24))
            .error(R.drawable.ic_baseline_face_24)
            .into(binding.ktp)

        Glide.with(this)
            .load(sertifikat)
            .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_face_24))
            .error(R.drawable.ic_baseline_face_24)
            .into(binding.keahlian)
        binding.progressBar.visibility = View.GONE


        binding.setuju.setOnClickListener {
            if (uid != null) {
                binding.progressBar.visibility = View.VISIBLE
                AddConsultant.verifyUser(uid, this)


                // todo 5555 itu delay dummy, soalnya saya blm tau cara nunggu proses diatas selesai duluan, makanya saya buat dummy delay
                Handler(Looper.getMainLooper()).postDelayed({
                    showDialogIfSuccess(name)
                }, 5555)
            }
        }

    }

    private fun showDialogIfSuccess(name: String?) {
        if (AddConsultant.result == true) {
            binding.progressBar.visibility = View.GONE
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Berhasil memverifikasi pengguna")
            builder.setMessage("Anda telah berhasil memverifikasi pengguna dengan nama: $name")
            builder.setPositiveButton("YES") { _, _ ->
                onBackPressed()
                onBackPressed()
            }.show()
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}