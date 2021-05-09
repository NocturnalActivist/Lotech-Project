package com.goat.lotech.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.goat.lotech.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun goToRegisterPage(view: View) {
        startActivity(Intent(view.context, SignUpActivity::class.java))
    }

    fun goToForgotPasswordPage(view: View) {
        startActivity(Intent(view.context, ForgotPasswordActivity::class.java))
    }

}