package com.goat.lotech.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.goat.lotech.R
import com.goat.lotech.databinding.ActivityForgotPasswordBinding
import com.goat.lotech.model.ForgotPassword

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            // check if any blank or wrong value in Email form
            emailFormValidation()
        }
    }

    private fun emailFormValidation() {
        val email = binding.editEmailLogin.text.toString().trim()

        if(!email.isValidEmail()) {
            binding.editEmailLogin.error = resources.getString(R.string.email_error)
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        ForgotPassword.forgotPassword(email, this)

        // todo 5555 itu delay dummy, soalnya saya blm tau cara nunggu proses diatas selesai duluan, makanya saya buat dummy delay
        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("TAG", ForgotPassword.result.toString())
            if(ForgotPassword.result == true) {
                showDialogIfSuccess()
            }
        }, 5555)


    }

    private fun showDialogIfSuccess() {
        // if user successfully confirmed forgot password, show alert dialog that notify user success confirmed
        if (ForgotPassword.result == true) {
            binding.progressBar.visibility = View.GONE
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.confirmedForgotPassowrdTitle)
            builder.setMessage(R.string.messageConfirmed)
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

    private fun String.isValidEmail() =
        isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

}