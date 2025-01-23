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

// Список аптек с координатами и названиями
        val pharmacies = listOf(
            Pair(GeoPoint(53.918750, 27.598160), "Государственная Аптека №1"),
            Pair(GeoPoint(53.924000, 27.574000), "Государственная Аптека №2"),
            Pair(GeoPoint(53.836600, 27.481200), "Государственная Аптека №3"),
            Pair(GeoPoint(53.891600, 27.552300), "Государственная Аптека №4"),
            Pair(GeoPoint(53.911000, 27.517800), "Государственная Аптека №5"),
            Pair(GeoPoint(53.876400, 27.621200), "Государственная Аптека №6"),
            Pair(GeoPoint(53.934500, 27.648700), "Государственная Аптека №7"),
            Pair(GeoPoint(53.902300, 27.490200), "Государственная Аптека №8"),
            Pair(GeoPoint(53.880900, 27.569500), "Государственная Аптека №9"),
            Pair(GeoPoint(53.917700, 27.604000), "Аптека №10"),
            Pair(GeoPoint(53.920000, 27.560000), "Аптека №11"),
            Pair(GeoPoint(53.910000, 27.540000), "Аптека №12"),
            Pair(GeoPoint(53.930000, 27.580000), "Аптека №13"),
            Pair(GeoPoint(53.950000, 27.590000), "Аптека №14"),
            Pair(GeoPoint(53.870000, 27.550000), "Аптека №15"),
            Pair(GeoPoint(53.940000, 27.600000), "Аптека №16"),
            Pair(GeoPoint(53.880000, 27.570000), "Аптека №17"),
            Pair(GeoPoint(53.860000, 27.520000), "Аптека №18"),
            Pair(GeoPoint(53.890000, 27.580000), "Аптека №19"),
            Pair(GeoPoint(53.900000, 27.600000), "Аптека №20"),
            Pair(GeoPoint(53.870500, 27.561200), "Аптека №21"),
            Pair(GeoPoint(53.876700, 27.634100), "Аптека №22"),
            Pair(GeoPoint(53.890100, 27.591400), "Аптека №23"),
            Pair(GeoPoint(53.903200, 27.523400), "Аптека №24"),
            Pair(GeoPoint(53.917800, 27.604800), "Аптека №25"),
            Pair(GeoPoint(53.921200, 27.564500), "Аптека №26"),
            Pair(GeoPoint(53.898800, 27.530300), "Аптека №27"),
            Pair(GeoPoint(53.935400, 27.654200), "Аптека №28"),
            Pair(GeoPoint(53.888700, 27.610300), "Аптека №29"),
            Pair(GeoPoint(53.884600, 27.570900), "Аптека №30"),
            Pair(GeoPoint(53.874400, 27.548800), "Аптека №31"),
            Pair(GeoPoint(53.861200, 27.525600), "Аптека №32"),
            Pair(GeoPoint(53.892300, 27.583200), "Аптека №33"),
            Pair(GeoPoint(53.900900, 27.614400), "Аптека №34"),
            Pair(GeoPoint(53.911300, 27.640200), "Аптека №35"),
            Pair(GeoPoint(53.919500, 27.553100), "Аптека №36"),
            Pair(GeoPoint(53.923400, 27.590800), "Аптека №37"),
            Pair(GeoPoint(53.930700, 27.605900), "Аптека №38"),
            Pair(GeoPoint(53.940500, 27.560400), "Аптека №39"),
            Pair(GeoPoint(53.880200, 27.590300), "Аптека №40")
        )

// Добавляем маркеры для каждой аптеки
        pharmacies.forEach { (geoPoint, title) ->
            val marker = Marker(mapView)
            marker.position = geoPoint
            marker.title = title
            mapView.overlays.add(marker)
        }


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
