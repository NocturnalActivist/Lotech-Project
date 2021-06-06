package com.goat.lotech.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.goat.lotech.databinding.ActivityConsultAddExpertBinding
import com.goat.lotech.model.AddConsultant
import com.goat.lotech.storage.ConsultAddManager

class ConsultAddExpertActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsultAddExpertBinding

    companion object {
        const val REQUEST_FROM_CAMERA_TO_SELF_PHOTO = 1001
        const val REQUEST_FROM_CAMERA_TO_KTP = 1002
        const val REQUEST_FROM_CAMERA_TO_SERTIFIKAT = 1003
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultAddExpertBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Menjadi Pakar Konsultasi"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.btnMendaftar.setOnClickListener {
            // form validation
            formValidation()
        }

        binding.uploadSelfPhoto.setOnClickListener {
            captureImageFromCamera(REQUEST_FROM_CAMERA_TO_SELF_PHOTO)
        }

        binding.uploadKTP.setOnClickListener {
            captureImageFromCamera(REQUEST_FROM_CAMERA_TO_KTP)
        }

        binding.uploadSertifikat.setOnClickListener {
            captureImageFromCamera(REQUEST_FROM_CAMERA_TO_SERTIFIKAT)
        }


    }

    private fun captureImageFromCamera(reqCode: Int) {
        ImagePicker.with(this)
            .cameraOnly()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start(reqCode)
    }

    private fun formValidation() {
        val name = binding.nameExpert.text.toString().trim()
        val description = binding.shortDescriptionExpert.text.toString()
        val sertifikatKeahlian = binding.sertifikatKeahlian.text.toString().trim()
        val noHp = binding.nomorHp.text.toString().trim()
        val selfPhoto = ConsultAddManager.selfPhoto
        val ktp = ConsultAddManager.ktp
        val sertifikat = ConsultAddManager.sertifikat

        when {
            name.isEmpty() -> {
                binding.nameExpert.error = "Nama Lengkap tidak boleh kosong"
                return
            }
            description.isEmpty() -> {
                binding.shortDescriptionExpert.error = "Deskripsi Singkat Tidak boleh kosong"
                return
            }
            sertifikatKeahlian.isEmpty() -> {
                binding.sertifikatKeahlian.error = "Sertifikat Keahlian tidak boleh kosong"
                return
            }
            noHp.isEmpty() -> {
                binding.nomorHp.error = "Nomor Handphone tidak boleh kosong"
                return
            }
            selfPhoto == null -> {
                Toast.makeText(this, "Foto Diri Anda tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }
            ktp == null -> {
                Toast.makeText(this, "Foto KTP Anda tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }
            sertifikat == null -> {
                Toast.makeText(this, "Foto Sertifikat Keahlian Anda tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }
        }

        binding.progressBar.visibility = View.VISIBLE
        AddConsultant.addData(name, description, sertifikatKeahlian, noHp, selfPhoto, ktp, sertifikat, this)
        // todo 7777 itu delay dummy, soalnya saya blm tau cara nunggu proses diatas selesai duluan, makanya saya buat dummy delay
        Handler(Looper.getMainLooper()).postDelayed({
            showDialogIfSuccess()
        }, 7777)

    }

    private fun showDialogIfSuccess() {
        if (AddConsultant.result == true) {
            binding.progressBar.visibility = View.GONE
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Berhasil Mendaftar")
            builder.setMessage("Anda telah berhasil mendaftar untuk menjadi Pakar Konsultasi\n\nLotech akan melakukan verifikasi terhadap data diri yang anda kirimkan\n\nMohon menunggu, Terima kasih :)")
            builder.setPositiveButton("YES") { _, _ ->
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Log.d("TAG", requestCode.toString())
            when (requestCode) {
                REQUEST_FROM_CAMERA_TO_SELF_PHOTO -> {
                    ConsultAddManager.uploadImageOption(this, data?.data!!, "selfphoto")
                }
                REQUEST_FROM_CAMERA_TO_KTP -> {
                    ConsultAddManager.uploadImageOption(this, data?.data!!, "ktp")
                }
                REQUEST_FROM_CAMERA_TO_SERTIFIKAT -> {
                    ConsultAddManager.uploadImageOption(this, data?.data!!, "sertifikat")
                }
            }
        }
    }
}
