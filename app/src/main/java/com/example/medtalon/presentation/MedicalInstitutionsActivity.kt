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
        org.osmdroid.config.Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        val mapView: MapView = findViewById(R.id.mapView)
        mapView.setMultiTouchControls(true)

        val startPoint = GeoPoint(53.9006, 27.5590)
        mapView.controller.setZoom(12.0)
        mapView.controller.setCenter(startPoint)

        val markerList = listOf(
            GeoPoint(53.918750, 27.598160) to "24 Поликлиника",
            GeoPoint(53.924000, 27.574000) to "1 Поликлиника",
            GeoPoint(53.836600, 27.481200) to "25 Поликлиника",
            GeoPoint(53.891000, 27.566000) to "2 Поликлиника",
            GeoPoint(53.900000, 27.550000) to "3 Поликлиника",
            GeoPoint(53.920000, 27.580000) to "4 Поликлиника",
            GeoPoint(53.895000, 27.540000) to "5 Поликлиника",
            GeoPoint(53.875000, 27.560000) to "6 Поликлиника",
            GeoPoint(53.810000, 27.470000) to "7 Поликлиника",
            GeoPoint(53.925000, 27.555000) to "8 Поликлиника",
            GeoPoint(53.880000, 27.530000) to "9 Поликлиника",
            GeoPoint(53.870000, 27.490000) to "10 Поликлиника",
            GeoPoint(53.885000, 27.600000) to "11 Поликлиника",
            GeoPoint(53.900500, 27.570000) to "12 Поликлиника",
            GeoPoint(53.860000, 27.550000) to "13 Поликлиника",
            GeoPoint(53.875500, 27.610000) to "14 Поликлиника",
            GeoPoint(53.900200, 27.520000) to "15 Поликлиника",
            GeoPoint(53.920500, 27.580500) to "16 Поликлиника",
            GeoPoint(53.840000, 27.590000) to "17 Поликлиника",
            GeoPoint(53.815000, 27.450000) to "18 Поликлиника",
            GeoPoint(53.865000, 27.520000) to "19 Поликлиника",
            GeoPoint(53.890000, 27.550000) to "20 Поликлиника",
            GeoPoint(53.850000, 27.600000) to "21 Поликлиника",
            GeoPoint(53.830000, 27.580000) to "22 Поликлиника",
            GeoPoint(53.865500, 27.570000) to "23 Поликлиника",
            GeoPoint(53.900100, 27.570500) to "26 Поликлиника",
            GeoPoint(53.880500, 27.590500) to "27 Поликлиника",
            GeoPoint(53.915000, 27.540000) to "28 Поликлиника",
            GeoPoint(53.825000, 27.460000) to "29 Поликлиника",
            GeoPoint(53.850500, 27.570500) to "30 Поликлиника",
            GeoPoint(53.870200, 27.540500) to "31 Поликлиника",
            GeoPoint(53.875000, 27.560500) to "32 Поликлиника",
            GeoPoint(53.890200, 27.530500) to "33 Поликлиника",
            GeoPoint(53.900800, 27.550500) to "34 Поликлиника",
            GeoPoint(53.905000, 27.560000) to "35 Поликлиника",
            GeoPoint(53.890100, 27.590500) to "36 Поликлиника",
            GeoPoint(53.860500, 27.530500) to "37 Поликлиника",
            GeoPoint(53.920000, 27.590000) to "38 Поликлиника",
            GeoPoint(53.830500, 27.470500) to "39 Поликлиника",
            GeoPoint(53.875500, 27.590500) to "40 Поликлиника"
        )

        markerList.forEach { (position, title) ->
            val marker = Marker(mapView).apply {
                this.position = position
                this.title = title
            }
            mapView.overlays.add(marker)
        }



        binding.toMapButton.setOnClickListener {
            binding.medicalInstitutionsListView.visibility = View.GONE
            binding.mapView.visibility = View.VISIBLE
        }

        binding.toListButton.setOnClickListener {
            binding.medicalInstitutionsListView.visibility = View.VISIBLE
            binding.mapView.visibility = View.GONE
        }

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
                this.polyclinics = polyclinics
                val query = intent.getStringExtra("QUERY")
                updateListView(polyclinics)
                query?.let { filterPolyclinics(it) }
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
