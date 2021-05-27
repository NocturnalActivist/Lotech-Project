package com.goat.lotech.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.goat.lotech.R
import com.goat.lotech.databinding.FragmentProfileBinding
import com.goat.lotech.model.Profile
import com.goat.lotech.model.ProfileModel
import com.goat.lotech.storage.ProfileManager
import com.goat.lotech.ui.activity.LoginActivity
import com.goat.lotech.viewmodel.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    companion object {
        const val REQUEST_FROM_GALLERY_TO_SELF_PHOTO = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        getUserData()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            logoutFromApplication()
        }

        binding.btnEdit.setOnClickListener {
            showUpdateProfileOption()
        }

        // todo: ini dihapus aja
        // setProfile()
    }

    private fun getUserData() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[ProfileViewModel::class.java]
        binding.progressBar.visibility = View.VISIBLE
        viewModel.setUser(uid)
        viewModel.getUser().observe(viewLifecycleOwner, { profile ->

            Log.d("TAG", profile.toString())

            if (profile != null) {
                getProfile(profile)
            }

        })
    }

    private fun showUpdateProfileOption() {
        val options = arrayOf(
            "Perbarui Foto Profil",
            "Perbarui Username",
            "Perbarui Tinggi Badan",
            "Perbarui Berat Badan"
        )

        val builder = android.app.AlertDialog.Builder(activity)
        builder.setTitle("Memerbarui Profil Pengguna")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> {
                    updateImageProfile()
                }
                1 -> {
                    updateUserData("Username", "name")
                    dialog.dismiss()
                }
                2 -> {
                    updateUserData("Tinggi Badan", "heightBody")
                    dialog.dismiss()
                }
                3 -> {
                    updateUserData("Berat Badan", "weightBody")
                    dialog.dismiss()
                }
            }
        }
        builder.create().show()
    }

    private fun updateUserData(option: String, child: String) {
        val builder = activity?.let { AlertDialog.Builder(it) }
        builder?.setTitle("Perbarui $option")
        val updateUserData = EditText(activity)
        if (option != "Username") {
            updateUserData.setRawInputType(InputType.TYPE_CLASS_NUMBER)
        } else {
            updateUserData.setRawInputType(InputType.TYPE_CLASS_TEXT)
        }

        builder?.setView(updateUserData)

        builder?.setPositiveButton("PERBARUI") { dialog, _ ->
            Profile.updateUserData(
                updateUserData.text.toString(),
                activity,
                child
            )

            when (option) {
                "Username" -> {
                    binding.nameProfile.text = updateUserData.text
                }
                "Tinggi Badan" -> {
                    binding.tvHeight.text = updateUserData.text
                }
                else -> {
                    binding.tvWeight.text = updateUserData.text
                }
            }
            dialog.dismiss()

        }
        builder?.setNegativeButton("TIDAK") { dialog, _ ->
            dialog.dismiss()
        }?.show()
    }

    private fun updateImageProfile() {
        ImagePicker.with(this)
            .galleryOnly()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start(REQUEST_FROM_GALLERY_TO_SELF_PHOTO)
    }

    @SuppressLint("SetTextI18n")
    private fun getProfile(data: ProfileModel) {
        binding.nameProfile.text = data.name
        binding.tvHeight.text = data.heightBody + " CM"
        binding.tvWeight.text = data.weightBody + " KG"
        binding.tvEmail.text = data.email
        binding.tvGender.text = data.gender
        binding.tvBirthdate.text = data.birthDate

        Glide.with(this)
            .load(data.image)
            .placeholder(R.drawable.ic_baseline_person_24)
            .error(R.drawable.ic_baseline_person_24)
            .into(binding.avatarProfile)

        binding.progressBar.visibility = View.GONE
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
            activity?.finish()

        }
        builder?.setNegativeButton("TIDAK") { dialog, _ ->
            dialog.dismiss()
        }?.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Log.d("TAG", requestCode.toString())
            when (requestCode) {
                REQUEST_FROM_GALLERY_TO_SELF_PHOTO -> {
                    activity?.let {
                        ProfileManager.uploadImageOption(it, data?.data!!)



                        binding.progressBar.visibility = View.VISIBLE
                        // todo 5555 itu delay dummy, soalnya saya blm tau cara nunggu proses diatas selesai duluan, makanya saya buat dummy delay
                        Handler(Looper.getMainLooper()).postDelayed({
                            val image = ProfileManager.image
                            Profile.updateImage(image, activity)

                            activity?.let { data ->
                                Glide.with(data)
                                    .load(image)
                                    .placeholder(R.drawable.ic_baseline_person_24)
                                    .error(R.drawable.ic_baseline_person_24)
                                    .into(binding.avatarProfile)

                                binding.progressBar.visibility = View.GONE
                            }
                        }, 5555)
                    }
                }
            }
        }
    }

}