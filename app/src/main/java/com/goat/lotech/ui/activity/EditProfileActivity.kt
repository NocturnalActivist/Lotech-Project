package com.goat.lotech.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.goat.lotech.R
import com.goat.lotech.databinding.ActivityEditProfileBinding
import com.goat.lotech.ui.fragment.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerFragment.DialogDateListener {

    private var binding: ActivityEditProfileBinding? = null

    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnOnceDate?.setOnClickListener(this)
        binding?.btnEdtProfile?.setOnClickListener(this)
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
}