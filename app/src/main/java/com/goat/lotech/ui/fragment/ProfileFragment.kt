package com.goat.lotech.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.goat.lotech.databinding.FragmentProfileBinding
import com.goat.lotech.ui.activity.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

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
        binding.btnLogout.setOnClickListener {
            logoutFromApplication()
        }
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