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

class PillsBookActivity : AppCompatActivity() {
    private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drug)

        val drugs = listOf(
            "Парацетамол: Жаропонижающее средство",
            "Ибупрофен: Противовоспалительное средство",
            "Амоксициллин: Антибиотик широкого спектра действия",
            "Аспирин: Анальгетик и жаропонижающее",
            "Лоратадин: Антигистаминное средство",
            "Цефтриаксон: Антибиотик широкого спектра действия",
            "Нимесулид: Противовоспалительное и обезболивающее средство",
            "Феназепам: Анксиолитик, седативное средство",
            "Диазепам: Противосудорожное средство",
            "Метформин: Препарат для снижения уровня сахара в крови",
            "Амлодипин: Препарат для лечения гипертонии",
            "Лизиноприл: Препарат для лечения гипертонии",
            "Диклофенак: Обезболивающее средство, противовоспалительное",
            "Амиодарон: Препарат для лечения аритмий",
            "Клопидогрел: Препарат для разжижения крови",
            "Гидрокортизон: Противовоспалительное средство",
            "Левофлоксацин: Антибиотик широкого спектра",
            "Пантопразол: Препарат для лечения заболеваний желудка",
            "Энап: Лекарство для лечения гипертонии",
            "Флуоксетин: Антидепрессант",
            "Моксонидин: Лекарство от гипертонии",
            "Ранитидин: Средство для лечения язвы желудка",
            "Ингавирин: Противовирусное средство",
            "Таблетки от кашля: Средство от кашля",
            "Амброксол: Средство от кашля",
            "Омепразол: Препарат для лечения язвы",
            "Кардиоаспирин: Средство для разжижения крови",
            "Симвастатин: Препарат для снижения уровня холестерина",
            "Бисопролол: Препарат для лечения гипертонии",
            "Глибенкламид: Препарат для лечения диабета",
            "Ксарелто: Препарат для профилактики тромбозов",
            "Эналаприл: Препарат для лечения гипертонии",
            "Кальций-Д3: Витаминно-минеральный комплекс",
            "Йодомарин: Средство для профилактики заболеваний щитовидной железы"
        )

        val drugDetails = mapOf(
            "Парацетамол" to "Жаропонижающее средство. Используется при высокой температуре и болях.",
            "Ибупрофен" to "Противовоспалительное средство, также помогает при болях.",
            "Амоксициллин" to "Антибиотик широкого спектра действия. Используется при бактериальных инфекциях.",
            "Аспирин" to "Анальгетик и жаропонижающее. Помогает при боли и воспалении.",
            "Лоратадин" to "Антигистаминное средство. Используется для лечения аллергии.",
            "Цефтриаксон" to "Антибиотик широкого спектра действия, используется для лечения инфекций.",
            "Нимесулид" to "Противовоспалительное и обезболивающее средство.",
            "Феназепам" to "Анксиолитик, седативное средство.",
            "Диазепам" to "Противосудорожное средство.",
            "Метформин" to "Препарат для снижения уровня сахара в крови.",
            "Амлодипин" to "Препарат для лечения гипертонии.",
            "Лизиноприл" to "Препарат для лечения гипертонии.",
            "Диклофенак" to "Обезболивающее средство, противовоспалительное.",
            "Амиодарон" to "Препарат для лечения аритмий.",
            "Клопидогрел" to "Препарат для разжижения крови.",
            "Гидрокортизон" to "Противовоспалительное средство.",
            "Левофлоксацин" to "Антибиотик широкого спектра.",
            "Пантопразол" to "Препарат для лечения заболеваний желудка.",
            "Энап" to "Лекарство для лечения гипертонии.",
            "Флуоксетин" to "Антидепрессант.",
            "Моксонидин" to "Лекарство от гипертонии.",
            "Ранитидин" to "Средство для лечения язвы желудка.",
            "Ингавирин" to "Противовирусное средство.",
            "Таблетки от кашля" to "Средство от кашля.",
            "Амброксол" to "Средство от кашля.",
            "Омепразол" to "Препарат для лечения язвы.",
            "Кардиоаспирин" to "Средство для разжижения крови.",
            "Симвастатин" to "Препарат для снижения уровня холестерина.",
            "Бисопролол" to "Препарат для лечения гипертонии.",
            "Глибенкламид" to "Препарат для лечения диабета.",
            "Ксарелто" to "Препарат для профилактики тромбозов.",
            "Эналаприл" to "Препарат для лечения гипертонии.",
            "Кальций-Д3" to "Витаминно-минеральный комплекс.",
            "Йодомарин" to "Средство для профилактики заболеваний щитовидной железы."
        )

        val query = intent.getStringExtra("query")?:""

        val sortedDrugs = drugs.sortedWith { drug1, drug2 ->
            // Преобразуем строки в нижний регистр для нечувствительности к регистру
            val containsQuery1 = drug1.lowercase().contains(query.lowercase())
            val containsQuery2 = drug2.lowercase().contains(query.lowercase())

            when {
                containsQuery1 && !containsQuery2 -> -1 // drug1 должен быть первым
                !containsQuery1 && containsQuery2 -> 1  // drug2 должен быть первым
                else -> 0 // Если оба содержат или не содержат query, оставляем как есть
            }
        }

        val listView: ListView = findViewById(R.id.drugListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, sortedDrugs)
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
