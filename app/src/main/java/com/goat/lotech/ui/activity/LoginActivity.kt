package com.goat.lotech.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.goat.lotech.databinding.ActivityLoginBinding
import com.goat.lotech.model.LoginModel
import com.goat.lotech.model.SignUpModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            // check if any blank or wrong value in signUp form
            signUpFormValidation()
        }

    }

    private fun signUpFormValidation() {
        val email = binding.editEmailLogin.text.toString().trim()
        val password = binding.tvForgotPassword.text.toString().trim()

        if (!email.isValidEmail()) {
            binding.editEmailLogin.error =
                "Maaf, perhatikan kembali email anda, terdapat kesalahan"
            return
        } else if (!password.isValidPassword()) {
            binding.editPasswordLogin.error =
                "Maaf, perhatikan kembali kata sandi anda, terdapat kesalahan"
            return
        }

        // Call SignUp Model for user registration, using coroutine
        binding.progressBar.visibility = View.VISIBLE
        LoginModel.signIn(email, password, this)



    }

    fun goToRegisterPage(view: View) {
        startActivity(Intent(view.context, SignUpActivity::class.java))
    }

    fun goToForgotPasswordPage(view: View) {
        startActivity(Intent(view.context, ForgotPasswordActivity::class.java))
    }

    private fun String.isValidEmail() =
        isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun String.isValidPassword() = length >= 6

}