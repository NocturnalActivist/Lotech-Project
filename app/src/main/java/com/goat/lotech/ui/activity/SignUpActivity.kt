package com.goat.lotech.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.goat.lotech.R
import com.goat.lotech.databinding.ActivitySignUpBinding
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity(), View.OnClickListener, DatePickerFragment.DialogDateListener {

    private var binding: ActivitySignUpBinding? = null

    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //Listener ke Date
        binding?.btnOnceDate?.setOnClickListener(this)
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
}