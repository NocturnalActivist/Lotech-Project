package com.goat.lotech.ml

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.goat.lotech.R
import com.goat.lotech.databinding.ActivityMlmainBinding
import com.goat.lotech.ui.activity.LoginActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.IOException


class MLMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMlmainBinding
    private lateinit var classifier: ImageClassifier

    internal lateinit var backgroundThread: HandlerThread
    internal lateinit var backgroundHandler: Handler

    internal val lock = Any()
    internal var runClassifier = false

    private val runPerically = object : Runnable {
        override fun run() {
            synchronized(lock) {
                if (runClassifier) {
                    classifyImage()
                    return
                }
            }
            backgroundHandler.post(this)
            return
        }
    }

    companion object {
        internal const val HANDLER_THREAD_NAME = "HANDLER_THREAD"
        private const val IMAGE_PICK_CAMERA_CODE = 112
        private const val IMAGE_PICK_GALLERY_CODE = 113
    }

    private val cnt = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMlmainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Cek Kalori Makananmu Hari Ini"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.chooseImageButton.setOnClickListener {

            val options = arrayOf("Dari Kamera", "Dari Galeri")

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Unggah Foto Makanan Melalui ?")
            builder.setItems(options) { _, which ->
                if (which == 0) {
                    //camera choose
                    uploadFromCamera()
                } else if (which == 1) {
                    //Gallery choose
                    uploadFromGallery()
                }
            }
            builder.create().show()

        }

        try {
            classifier = ImageClassifierQuantizedMobileNet(this)
        } catch (e: IOException) {
            Log.e("Classifier", "unable to init classifier")
            e.printStackTrace()
        }

        startBackgroundThread()
    }

    private fun uploadFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(
            Intent.createChooser(intent, "Pick a Picture"),
            IMAGE_PICK_GALLERY_CODE
        )
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun uploadFromCamera() {
        ImagePicker.with(this)
            .cameraOnly()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start(IMAGE_PICK_CAMERA_CODE)
        binding.progressBar.visibility = View.VISIBLE
    }


    override fun onDestroy() {
        stopBackgroundThread()
        classifier.close()
        super.onDestroy()
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun classifyImage() {
        val originalBitmap: Bitmap = (binding.selectedImageView.drawable as BitmapDrawable).bitmap
        val scaleDrawable = Bitmap.createScaledBitmap(
            originalBitmap,
            classifier.imageSizeX,
            classifier.imageSizeY,
            false
        )

        val text = classifier.classifyFrame(scaleDrawable)

        scaleDrawable.recycle()

        //runOnUiThread { binding.predictionText.text = text }


        if(text.isNotEmpty()) {
            loadDatasetFromFirebase(text.toString())
        }
    }

    private fun loadDatasetFromFirebase(namaMakanan: String) {
        Firebase.firestore
            .collection("dataset")
            .document(namaMakanan)
            .get()
            .addOnSuccessListener {
                binding.progressBar.visibility = View.GONE

                val builder = AlertDialog.Builder(this)
                builder.setTitle(namaMakanan)
                builder.setMessage("Kalori yang terkandung dalam 100 gram $namaMakanan yaitu ${it["energi(kal)"].toString()} kalori")
                builder.setCancelable(true)
                builder.setPositiveButton("YES") { dialog, _ ->
                    dialog.dismiss()
                }.show()

            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "gagal: ${it.printStackTrace()}", Toast.LENGTH_SHORT).show()
                Log.e("Error", it.toString())
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CAMERA_CODE || requestCode == IMAGE_PICK_GALLERY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val selectedImageUri: Uri? = data?.data
                if (selectedImageUri != null) {
                    val inputStream = contentResolver.openInputStream(selectedImageUri)
                    val selectedImage = BitmapFactory.decodeStream(inputStream)
                    binding.selectedImageView.setImageBitmap(selectedImage)
                    backgroundHandler.post(runPerically)
                }

            }
        }
    }
}