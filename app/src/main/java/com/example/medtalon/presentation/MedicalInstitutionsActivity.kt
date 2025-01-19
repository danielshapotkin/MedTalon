package com.example.medtalon.presentation

import Polyclinic
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.medtalon.adapters.PolyclinicAdapter
import com.example.medtalon.data.DataBase
import com.example.test2.R
import com.example.test2.databinding.ActivityMedicalInstitutionsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker




class MedicalInstitutionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicalInstitutionsBinding
    private val dataBase: DataBase = DataBase.getInstance()
    private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()
    private var polyclinics: List<Polyclinic> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicalInstitutionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        org.osmdroid.config.Configuration.getInstance().load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext))
        val mapView: MapView = findViewById(R.id.mapView)
        mapView.setMultiTouchControls(true)

        val startPoint = GeoPoint(53.9006, 27.5590)
        mapView.controller.setZoom(12.0)
        mapView.controller.setCenter(startPoint)

        val marker = Marker(mapView).apply {
            position = startPoint
            title = "Минск"
        }
        mapView.overlays.add(marker)

        val marker24 = Marker(mapView).apply {
            position = GeoPoint(53.918750, 27.598160)
            title = "24 Поликлиника"
        }
        mapView.overlays.add(marker24)

        val marker1 = Marker(mapView).apply {
            position = GeoPoint(53.924000, 27.574000)
            title = "1 Поликлиника"
        }
        mapView.overlays.add(marker1)

        val marker25 = Marker(mapView).apply {
            position = GeoPoint(53.836600, 27.481200)
            title = "25 Поликлиника"
        }
        mapView.overlays.add(marker25)
        val marker2 = Marker(mapView).apply {
            position = GeoPoint(53.891000, 27.566000)
            title = "2 Поликлиника"
        }
        mapView.overlays.add(marker2)

        val marker3 = Marker(mapView).apply {
            position = GeoPoint(53.900000, 27.550000)
            title = "3 Поликлиника"
        }
        mapView.overlays.add(marker3)

        val marker4 = Marker(mapView).apply {
            position = GeoPoint(53.920000, 27.580000)
            title = "4 Поликлиника"
        }
        mapView.overlays.add(marker4)

        val marker5 = Marker(mapView).apply {
            position = GeoPoint(53.895000, 27.540000)
            title = "5 Поликлиника"
        }
        mapView.overlays.add(marker5)

        val marker6 = Marker(mapView).apply {
            position = GeoPoint(53.875000, 27.560000)
            title = "6 Поликлиника"
        }
        mapView.overlays.add(marker6)

        binding.toMapButton.setOnClickListener {
                binding.medicalInstitutionsListView.visibility = View.GONE
                binding.mapView.visibility = View.VISIBLE
        }

        binding.toListButton.setOnClickListener{
            binding.medicalInstitutionsListView.visibility = View.VISIBLE
            binding.mapView.visibility = View.GONE
        }

        homeViewModel.selectedRegion.observe(this) { region ->
            binding.titleTw.text = "Медицинские учреждения\n $region"
        }

       binding.backButton.setOnClickListener{
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
                this.polyclinics = polyclinics
                updateListView(polyclinics)
            } else {
                println("Ошибка при получении поликлиник: $error")
            }
        }

        binding.searchButton.setOnClickListener {
            filterPolyclinics(binding.searchEditText.text.toString())
        }
    }



    private fun updateListView(polyclinics: List<Polyclinic>) {
        val adapter = PolyclinicAdapter(this, polyclinics)
        binding.medicalInstitutionsListView.adapter = adapter
    }

    private fun filterPolyclinics(query: String) {
        val filteredPolyclinics =
            polyclinics.sortedByDescending { it.name.contains(query, ignoreCase = true) }
        updateListView(filteredPolyclinics)
    }
}
