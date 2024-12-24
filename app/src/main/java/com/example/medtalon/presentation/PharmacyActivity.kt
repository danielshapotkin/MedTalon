package com.example.medtalon.presentation

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.test2.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class PharmacyActivity : AppCompatActivity() {
    private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacy)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

            org.osmdroid.config.Configuration.getInstance().load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext))
            val mapView: MapView = findViewById(R.id.mapView)
            mapView.setMultiTouchControls(true)

            val startPoint = GeoPoint(53.9006, 27.5590)
            mapView.controller.setZoom(12.0)
            mapView.controller.setCenter(startPoint)

        val marker1 = Marker(mapView).apply {
            position = GeoPoint(53.918750, 27.598160)
            title = "Государственная Аптека №1"
        }
        mapView.overlays.add(marker1)

        val marker2 = Marker(mapView).apply {
            position = GeoPoint(53.924000, 27.574000)
            title = "Государственная Аптека №2"
        }
        mapView.overlays.add(marker2)

        val marker3 = Marker(mapView).apply {
            position = GeoPoint(53.836600, 27.481200)
            title = "Государственная Аптека №3"
        }
        mapView.overlays.add(marker3)

        val marker4 = Marker(mapView).apply {
            position = GeoPoint(53.891600, 27.552300)
            title = "Государственная Аптека №4"
        }
        mapView.overlays.add(marker4)

        val marker5 = Marker(mapView).apply {
            position = GeoPoint(53.911000, 27.517800)
            title = "Государственная Аптека №5"
        }
        mapView.overlays.add(marker5)

        val marker6 = Marker(mapView).apply {
            position = GeoPoint(53.876400, 27.621200)
            title = "Государственная Аптека №6"
        }
        mapView.overlays.add(marker6)

        val marker7 = Marker(mapView).apply {
            position = GeoPoint(53.934500, 27.648700)
            title = "Государственная Аптека №7"
        }
        mapView.overlays.add(marker7)

        val marker8 = Marker(mapView).apply {
            position = GeoPoint(53.902300, 27.490200)
            title = "Государственная Аптека №8"
        }
        mapView.overlays.add(marker8)

        val marker9 = Marker(mapView).apply {
            position = GeoPoint(53.880900, 27.569500)
            title = "Государственная Аптека №9"
        }
        mapView.overlays.add(marker9)

        val marker10 = Marker(mapView).apply {
            position = GeoPoint(53.917700, 27.604000)
            title = "Аптека №10"
        }
        mapView.overlays.add(marker10)

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener{
            finish()
        }

        val profileButton = findViewById<ImageButton>(R.id.profile_button)
        profileButton.setOnClickListener {
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

        }
    }
