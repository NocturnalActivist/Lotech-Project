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
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.goat.lotech.databinding.ActivityMlmainBinding
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
                }
            }
            backgroundHandler.post(this)
        }
    }

    companion object {
        internal const val HANDLER_THREAD_NAME = "HANDLER_THREAD"
        private const val IMAGE_PICK_CAMERA_CODE = 112
        private const val IMAGE_PICK_GALLERY_CODE = 113
    }

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

        runOnUiThread { binding.predictionText.text = text }


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
                    binding.progressBar.visibility = View.GONE
                }

            }
        }
    }
}