package com.goat.lotech.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.goat.lotech.R
import com.goat.lotech.databinding.ActivitySignUpBinding
import com.goat.lotech.model.SignUp
import com.goat.lotech.ui.fragment.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerFragment.DialogDateListener {

    private var binding: ActivitySignUpBinding? = null
    private var gender: String? = null

    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //Listener ke Date
        binding?.btnOnceDate?.setOnClickListener(this)
        binding?.btnSignup?.setOnClickListener(this)

    }

    fun backToLoginPage(view: View) {
        val intent = Intent(view.context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_once_date -> {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, DATE_PICKER_TAG)
            }
            R.id.btn_signup -> {
                // check if any blank or wrong value in signUp form
                signUpFormValidation()
            }
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.male ->
                    if (checked) {
                        gender = "Laki-laki"
                    }
                R.id.female ->
                    if (checked) {
                        gender = "Perempuan"
                    }
            }
        }
    }

    private fun signUpFormValidation() {
        val name = binding?.editNameSignup?.text.toString().trim()
        val email = binding?.editEmailSignup?.text.toString().trim()
        val password = binding?.editPasswordSignup?.text.toString().trim()
        val heightBody = binding?.editHeightSignup?.text.toString().trim()
        val weightBody = binding?.editWeightSignup?.text.toString().trim()
        val birthDate = binding?.tvOnceDate?.text.toString().trim()

        if (!name.isValidName()) {
            binding?.editNameSignup?.error = "Maaf, nama harus diisi"
            return
        } else if (!email.isValidEmail()) {
            binding?.editEmailSignup?.error =
                "Maaf, perhatikan kembali email anda, terdapat kesalahan"
            return
        } else if (!password.isValidPassword()) {
            Toast.makeText(
                this,
                "Kata sandi minimal 6 karakter",
                Toast.LENGTH_LONG
            ).show()
            return
        } else if (!heightBody.isValidName()) {
            binding?.editHeightSignup?.error = "Maaf, tinggi badan harus diisi"
            return
        } else if (!weightBody.isValidName()) {
            binding?.editWeightSignup?.error = "Maaf, berat badan harus diisi"
            return
        } else if (birthDate == "Tanggal Lahir") {
            Toast.makeText(
                this,
                "Mohon isi tanggal lahir dengan menekan tombol kalender",
                Toast.LENGTH_LONG
            ).show()
            return
        } else if (gender == null) {
            Toast.makeText(
                this,
                "Mohon isi jenis kelamin",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // Call SignUp Model for user registration, using coroutine
        binding?.progressBar?.visibility = View.VISIBLE
        SignUp.register(email, password, name, heightBody, weightBody, gender, birthDate, this)

        // todo 7777 itu delay dummy, soalnya saya blm tau cara nunggu proses diatas selesai duluan, makanya saya buat dummy delay
        Handler(Looper.getMainLooper()).postDelayed({
                showDialogIfSuccess()
        }, 7777)


    }

    private fun showDialogIfSuccess() {
        // if user successfully registered, show alert dialog that notify user success registered
        Log.d("HASIL", SignUp.result.toString())
        if (SignUp.result == true) {
            binding?.progressBar?.visibility = View.GONE
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.registered)
            builder.setMessage(R.string.messageRegistered)
            builder.setPositiveButton("YES") { _, _ ->
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }.show()
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        //set untuk TextView
        binding?.tvOnceDate?.text = dateFormat.format(calendar.time)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun String.isValidName() = isNotEmpty()

    private fun String.isValidEmail() =
        isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun String.isValidPassword() = length >= 6

}