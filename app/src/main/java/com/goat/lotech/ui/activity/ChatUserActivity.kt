package com.goat.lotech.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.goat.lotech.databinding.ActivityChatUserBinding
import com.goat.lotech.model.Chat
import com.goat.lotech.model.Login
import com.goat.lotech.viewmodel.adapter.MessageAdapter
import com.goat.lotech.viewmodel.viewmodel.MessageViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class ChatUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatUserBinding
    private lateinit var viewModel: MessageViewModel

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


        showMessage(uid)

        sendMessage(name, selfPhoto, uid, myName, mySelfPhoto)


    }

    private fun showMessage(uid: String?) {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MessageViewModel::class.java]
        binding.progressBar.visibility = View.VISIBLE

        val myUid = FirebaseAuth.getInstance().currentUser?.uid.toString()

        val linearLayout = LinearLayoutManager(this)
        linearLayout.stackFromEnd = true
        binding.rvChat.layoutManager = linearLayout
        val adapter = MessageAdapter(myUid)
        adapter.notifyDataSetChanged()
        binding.rvChat.adapter = adapter

        if (uid != null) {
            viewModel.setMessage(myUid, uid)
            viewModel.getMessage().observe(this, { message ->
                if (message != null) {
                    binding.progressBar.visibility = View.GONE
                    adapter.setData(message)
                }
            })
        }
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

            if (message.isEmpty()) {
                Toast.makeText(this, "Pesan tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            @SuppressLint("SimpleDateFormat")
            val simpleDateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm:ss")
            val format: String = simpleDateFormat.format(Date())
            val myUid = FirebaseAuth.getInstance().currentUser?.uid.toString()

            Chat.sendChat(message, name, selfPhoto, format, uid, myName, mySelfPhoto, myUid)
            binding.chatBox.text.clear()

            showMessage(uid)



        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}