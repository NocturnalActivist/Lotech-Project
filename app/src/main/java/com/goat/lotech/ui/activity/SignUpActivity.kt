package com.goat.lotech.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.goat.lotech.R
import com.goat.lotech.databinding.ActivitySignUpBinding
import com.goat.lotech.model.SignUpModel


class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val TAG = SignUpActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {
            // check if any blank or wrong value in signUp form
            signUpFormValidation()
        }
    }

    private fun signUpFormValidation() {
        val name = binding.editNameSignup.text.toString().trim()
        val email = binding.editEmailSignup.text.toString().trim()
        val password = binding.editPasswordSignup.text.toString().trim()

        if (!name.isValidName()) {
            binding.editNameSignup.error = "Maaf, nama harus diisi"
            return
        } else if (!email.isValidEmail()) {
            binding.editEmailSignup.error =
                "Maaf, perhatikan kembali email anda, terdapat kesalahan"
            return
        } else if (!password.isValidPassword()) {
            binding.inputPasswordSignup.error =
                "Maaf, perhatikan kembali kata sandi anda, terdapat kesalahan"
            return
        }

        // Call SignUp Model for user registration, using coroutine
        binding.progressBar.visibility = View.VISIBLE
        SignUpModel.register(email, password, name, this)

        // todo 7777 itu delay dummy, soalnya saya blm tau cara nunggu proses diatas selesai duluan, makanya saya buat dummy delay
        Handler(Looper.getMainLooper()).postDelayed({
            showDialogIfSuccess()
        }, 7777)


    }

    private fun showDialogIfSuccess() {
        // if user successfully registered, show alert dialog that notify user success registered
        if (SignUpModel.result) {
            binding.progressBar.visibility = View.GONE
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.registered)
            builder.setMessage(R.string.messageRegistered)
            builder.setPositiveButton("YES") { _, _ ->
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }.show()
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    fun backToLoginPage(view: View) {
        val intent = Intent(view.context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun String.isValidName() = isNotEmpty()

    private fun String.isValidEmail() =
        isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun String.isValidPassword() = length >= 6

}