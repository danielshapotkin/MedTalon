package com.example.medtalon.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.test2.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class DrugActivity : AppCompatActivity() {
    private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drug)

        val drugs = listOf(
            "Парацетамол: Жаропонижающее средство",
            "Ибупрофен: Противовоспалительное средство",
            "Амоксициллин: Антибиотик широкого спектра действия",
            "Аспирин: Анальгетик и жаропонижающее",
            "Лоратадин: Антигистаминное средство"
        )

        val drugDetails = mapOf(
            "Парацетамол" to "Жаропонижающее средство. Используется при высокой температуре и болях.",
            "Ибупрофен" to "Противовоспалительное средство, также помогает при болях.",
            "Амоксициллин" to "Антибиотик широкого спектра действия. Используется при бактериальных инфекциях.",
            "Аспирин" to "Анальгетик и жаропонижающее. Помогает при боли и воспалении.",
            "Лоратадин" to "Антигистаминное средство. Используется для лечения аллергии."
        )

        val listView: ListView = findViewById(R.id.drugListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, drugs)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedDrug = drugs[position].split(":")[0]
            val selectedDrugInfo = drugDetails[selectedDrug] ?: "Информация недоступна"

            val intent = Intent(this, DrugDetailsActivity::class.java).apply {
                putExtra("drugName", selectedDrug)
                putExtra("drugInfo", selectedDrugInfo)
            }
            startActivity(intent)
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
