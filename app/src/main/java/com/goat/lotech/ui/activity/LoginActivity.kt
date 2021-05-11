package com.goat.lotech.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.goat.lotech.databinding.ActivityLoginBinding
import com.goat.lotech.model.Login

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            loginFormValidation()
        }

    }

    private fun loginFormValidation() {
        val email = binding.editEmailLogin.text.toString().trim()
        val password = binding.editPasswordLogin.text.toString().trim()

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
        Login.login(email, password,this)

        // todo 5555 itu delay dummy, soalnya saya blm tau cara nunggu proses diatas selesai duluan, makanya saya buat dummy delay
        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("TAG", Login.result.toString())
            binding.progressBar.visibility = View.GONE
            if(Login.result == true) {
                Toast.makeText(this, "MENUJU HALAMAN BERANDA", Toast.LENGTH_SHORT).show()
            }
        }, 5555)

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