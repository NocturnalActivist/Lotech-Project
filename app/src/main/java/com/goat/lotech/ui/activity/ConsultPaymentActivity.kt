package com.goat.lotech.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.goat.lotech.R
import com.goat.lotech.databinding.ActivityConsultPaymentBinding
import com.goat.lotech.model.AddConsultant
import com.goat.lotech.storage.ConsultAddManager
import com.goat.lotech.storage.ProfileManager
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class ConsultPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsultPaymentBinding

    companion object {
        const val EXTRA_UID = "uid"
        const val EXTRA_NAME = "name"
        const val EXTRA_PRICE = "price"
        const val REQUEST_FROM_GALLERY_TO_SELF_PHOTO = 1001
    }

    var pakarName: String = ""
    var pakarUid: String = ""
    var price: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Pembayaran Konsultasi"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pakarName = intent.getStringExtra(EXTRA_NAME).toString()
        pakarUid = intent.getStringExtra(EXTRA_UID).toString()
        price = intent.getStringExtra(EXTRA_PRICE).toString()
        binding.tvTransaction.text = "Silahkan melakukan pembayaran secara transfer melalui nomor rekening berikut : 1234567890, atas nama PT.Lotech Indonesia sejumlah $price, untuk dapat melakukan konsultasi dengan $pakarName, \n\nKemudian unggah bukti transfer untuk kami verifikasi, Terima Kasih"

        validateForm()


    }

    private fun validateForm() {
        binding.imgBukti.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(REQUEST_FROM_GALLERY_TO_SELF_PHOTO)

            binding.progressBar.visibility = View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    @SuppressLint("SimpleDateFormat")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Log.d("TAG", requestCode.toString())
            when (requestCode) {
                REQUEST_FROM_GALLERY_TO_SELF_PHOTO -> {

                    Glide.with(this)
                        .load(data?.data)
                        .placeholder(R.drawable.ic_baseline_image_24)
                        .error(R.drawable.ic_baseline_image_24)
                        .into(binding.imgBukti)

                    ConsultAddManager.uploadBuktiTransfer(this, data?.data!!)

                    // todo 5555 itu delay dummy, soalnya saya blm tau cara nunggu proses diatas selesai duluan, makanya saya buat dummy delay
                    Handler(Looper.getMainLooper()).postDelayed({
                        val image = ProfileManager.image
                        val simpleDateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm:ss")
                        val format: String = simpleDateFormat.format(Date())
                        val myUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
                        val timeInMillis = System.currentTimeMillis().toString()
                        AddConsultant.uploadBuktiTransfer(image, this, format, myUid, pakarName, pakarUid, timeInMillis, price)

                        binding.progressBar.visibility = View.GONE
                    }, 5555)

                }
            }
        }
    }
}