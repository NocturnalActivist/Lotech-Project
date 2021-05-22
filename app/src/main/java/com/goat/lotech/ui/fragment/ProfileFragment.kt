package com.goat.lotech.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.goat.lotech.databinding.FragmentProfileBinding
import com.goat.lotech.model.ProfileModel
import com.goat.lotech.ui.activity.LoginActivity
import com.goat.lotech.viewmodel.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    companion object {
        const val EXTRA_USER ="user"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ProfileViewModel::class.java]
        viewModel.setUser()
        viewModel.getUser().observe(viewLifecycleOwner, { profile ->

            if(profile != null) {
                getProfile(profile)
            }
            Toast.makeText(context, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
        })

        binding.btnLogout.setOnClickListener {
            logoutFromApplication()
        }

        setProfile()
    }

    private fun getProfile(data: ProfileModel) {
        binding.nameProfile.text = data.name
        binding.tvHeight.text = data.heightBody
        binding.tvWeight.text = data.weightBody
        binding.tvEmail.text = data.email
        binding.tvGender.text = data.gender
        binding.tvBirthdate.text = data.birthDate

    }

    private fun setProfile() {
//        val bundle: Bundle = this.arguments()
        val user = arguments?.getParcelable<ProfileModel>(EXTRA_USER) as ProfileModel
        val uid = user.uid
        val email = user.email
        val name = user.name
        val heightBody = user.heightBody
        val weightBody = user.weightBody
        val gender = user.gender
        val birthdate = user.birthDate

        binding.nameProfile.text = name
        binding.tvHeight.text = heightBody
        binding.tvWeight.text = weightBody
        binding.tvEmail.text = email
        binding.tvGender.text = gender
        binding.tvBirthdate.text = birthdate
    }

    private fun logoutFromApplication() {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle("Konfirmasi Keluar")
        builder?.setMessage("Apakah anda yakin ingin keluar dari akun ini ?")
        builder?.setPositiveButton("YA") { dialog, _ ->

            // sign out from firebsae
            FirebaseAuth.getInstance().signOut()

            // go to login activity
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            dialog.dismiss()
            startActivity(intent)

        }
        builder?.setNegativeButton("TIDAK") { dialog, _ ->
            dialog.dismiss()
        }?.show()
    }


}