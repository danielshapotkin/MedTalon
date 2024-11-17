package com.example.medtalon.presentation

import Polyclinic
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.medtalon.data.DataBase
import com.example.test2.R
import com.example.test2.databinding.ActivityMedicalInstitutionsBinding

class MedicalInstitutionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicalInstitutionsBinding
    private val dataBase: DataBase = DataBase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicalInstitutionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Загружаем данные поликлиник из базы
        dataBase.getPolyclinics { success, polyclinics, error ->
            if (success && polyclinics != null) {
                // Отображаем данные в ListView
                updateListView(polyclinics)
            } else {
                println("Ошибка при получении поликлиник: $error")
            }
        }
    }

    /**
     * Обновляет ListView с данными о поликлиниках.
     */
    private fun updateListView(polyclinics: List<Polyclinic>) {
        // Создаем список строк для отображения
        val items = polyclinics.map { "${it.name}\nАдрес: ${it.adress}, Время работы: ${it.workTime}, Email: ${it.email}, Url: ${it.url} " }


        // Устанавливаем адаптер для ListView
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, // Простая разметка для каждого элемента
            items
        )
        binding.medicalInstitutionsListView.adapter = adapter
    }
}
