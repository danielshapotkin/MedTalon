package com.example.medtalon.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.medtalon.PaidServicesAdapter
import com.example.medtalon.data.DataBase
import com.example.medtalon.domain.PaidService
import com.example.test2.R
import com.example.test2.databinding.ActivityPaidServicesBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class PaidServices : AppCompatActivity() {

    private lateinit var binding: ActivityPaidServicesBinding
    private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()
    private val dataBase: DataBase = DataBase.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPaidServicesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
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

        dataBase.getPaidServices { success, paidServices, error ->
            if (success && paidServices != null) {
                updateListView(paidServices)
            } else {
                println("Ошибка при получении поликлиник: $error")
            }
        }
    }

    private fun updateListView(polyclinics: List<PaidService>) {
        val adapter = PaidServicesAdapter(this, polyclinics)
        binding.paidServicesListView.adapter = adapter
    }

}
