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

        // Применяем отступы для системы
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Загружаем настройки для OpenStreetMap
        org.osmdroid.config.Configuration.getInstance().load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext))

        // Инициализация карты
        val mapView: MapView = findViewById(R.id.mapView)
        mapView.setMultiTouchControls(true)

        val startPoint = GeoPoint(53.9006, 27.5590)
        mapView.controller.setZoom(12.0)
        mapView.controller.setCenter(startPoint)

        // Добавляем маркеры для аптек
        val pharmacies = listOf(
            GeoPoint(53.918750, 27.598160) to "Государственная Аптека №1",
            GeoPoint(53.924000, 27.574000) to "Государственная Аптека №2",
            GeoPoint(53.836600, 27.481200) to "Государственная Аптека №3",
            GeoPoint(53.891600, 27.552300) to "Государственная Аптека №4",
            GeoPoint(53.911000, 27.517800) to "Государственная Аптека №5",
            GeoPoint(53.876400, 27.621200) to "Государственная Аптека №6",
            GeoPoint(53.934500, 27.648700) to "Государственная Аптека №7",
            GeoPoint(53.902300, 27.490200) to "Государственная Аптека №8",
            GeoPoint(53.880900, 27.569500) to "Государственная Аптека №9",
            GeoPoint(53.917700, 27.604000) to "Аптека №10",
            GeoPoint(53.928750, 27.650300) to "Государственная Аптека №11",
            GeoPoint(53.875600, 27.540700) to "Государственная Аптека №12",
            GeoPoint(53.870100, 27.570900) to "Государственная Аптека №13",
            GeoPoint(53.876700, 27.610400) to "Государственная Аптека №14",
            GeoPoint(53.930000, 27.630000) to "Аптека №15",
            GeoPoint(53.899000, 27.558800) to "Государственная Аптека №16",
            GeoPoint(53.914500, 27.528900) to "Государственная Аптека №17",
            GeoPoint(53.918300, 27.564500) to "Государственная Аптека №18",
            GeoPoint(53.886400, 27.597800) to "Государственная Аптека №19",
            GeoPoint(53.876800, 27.580300) to "Аптека №20"
        )

        // Добавляем маркеры на карту
        pharmacies.forEach { (location, title) ->
            val marker = Marker(mapView).apply {
                position = location
                this.title = title
            }
            mapView.overlays.add(marker)
        }

        // Обработка кнопки назад
        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        // Обработка кнопки профиля
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
