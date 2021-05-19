package com.goat.lotech.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.goat.lotech.databinding.ActivityChatUserBinding
import com.goat.lotech.model.Chat
import java.text.SimpleDateFormat
import java.util.*

class ChatUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatUserBinding

    companion object {
        const val NAME = "name"
        const val SELF_PHOTO = "self_photo"
        const val UID = "uid"
        const val MY_NAME = "myName"
        const val MY_SELF_PHOTO = "mySelfPhoto"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(NAME)
        val selfPhoto = intent.getStringExtra(SELF_PHOTO)
        val uid = intent.getStringExtra(UID)
        val myName = intent.getStringExtra(MY_NAME)
        val mySelfPhoto = intent.getStringExtra(MY_SELF_PHOTO)

        supportActionBar?.title = name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sendMessage(name, selfPhoto, uid, myName, mySelfPhoto)


    }

    private fun sendMessage(
        name: String?,
        selfPhoto: String?,
        uid: String?,
        myName: String?,
        mySelfPhoto: String?
    ) {
        binding.sendBtn.setOnClickListener {
            val message = binding.chatBox.text.toString().trim()

            if(message.isEmpty()) {
                Toast.makeText(this, "Pesan tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            @SuppressLint("SimpleDateFormat")
            val simpleDateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm:ss")
            val format: String = simpleDateFormat.format(Date())

            Chat.sendChat(message, name, selfPhoto,format, uid, myName, mySelfPhoto)
            binding.chatBox.text.clear()

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}