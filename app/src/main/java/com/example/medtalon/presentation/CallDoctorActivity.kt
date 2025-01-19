package com.example.medtalon.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuInflater
import android.widget.Button
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.test2.R
import com.example.test2.databinding.ActivityCallDoctorBinding
import com.example.test2.databinding.ActivityGetTalonBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class CallDoctorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCallDoctorBinding
    private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeViewModel: HomeViewModel = HomeViewModel.getInstance()
        binding = ActivityCallDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.profileButton.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
            view.findViewById<Button>(R.id.view_profile_button).setOnClickListener {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                bottomSheetDialog.dismiss()
            }

            view.findViewById<Button>(R.id.settings_button).setOnClickListener {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                bottomSheetDialog.dismiss()
            }

            view.findViewById<Button>(R.id.logout_button).setOnClickListener {
                homeViewModel.logout()
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        }

        binding.showMoreInfo.setOnClickListener {
            val url = "https://26poliklinika.by"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        homeViewModel.selectedPolyclinic.observe(this){polyclinic->
            binding.polyclinicEditText.text = polyclinic
        }
    }
}