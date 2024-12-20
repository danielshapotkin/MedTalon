package com.example.medtalon.presentation

import Polyclinic
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.medtalon.adapters.PolyclinicAdapter
import com.example.medtalon.data.DataBase
import com.example.test2.R
import com.example.test2.databinding.ActivityMedicalInstitutionsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class MedicalInstitutionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicalInstitutionsBinding
    private val dataBase: DataBase = DataBase.getInstance()
    private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicalInstitutionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeViewModel.selectedRegion.observe(this) { region ->
        binding.titleTw.text = "Медицинские учреждения\n $region"
        }

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

        dataBase.getPolyclinics { success, polyclinics, error ->
            if (success && polyclinics != null) {
                val adapter = PolyclinicAdapter(this, polyclinics)
                binding.medicalInstitutionsListView.adapter = adapter
            } else {
                println("Ошибка при получении поликлиник: $error")
            }
        }

        binding.searchButton.setOnClickListener{

        }
    }
}
