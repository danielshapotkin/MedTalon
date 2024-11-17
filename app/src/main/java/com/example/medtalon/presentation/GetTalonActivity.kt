package com.example.medtalon.presentation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.MenuInflater
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medtalon.data.DataBase
import com.example.medtalon.domain.Doctor
import com.example.test2.R
import com.example.test2.databinding.ActivityGetTalonBinding
import com.example.test2.databinding.ActivityMainBinding
import java.util.Calendar

@Suppress("NAME_SHADOWING")
class GetTalonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetTalonBinding
    private val dataBase: DataBase = DataBase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetTalonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.profileButton.setOnClickListener{
            val popup = PopupMenu(this, it)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.profile_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.view_profile -> {

                        true
                    }
                    R.id.logout -> {

                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        binding.selectDateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                binding.selectedDate.text = selectedDate
            }, year, month, day)
            datePickerDialog.show()
        }

        // Установка обработчика нажатия для выбора времени
        binding.selectTimeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                binding.selectedTime.text = selectedTime
            }, hour, minute, true)
            timePickerDialog.show()
        }

        dataBase.getDoctors { success, doctors, error ->
            if (success && doctors != null) {
                updateSpinner(doctors)
            } else {
                Toast.makeText(this, "Ошибка загрузки врачей: $error", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun updateSpinner(doctors: List<Doctor>) {
        // Создаем список имен врачей
        val doctorNames = doctors.map { "${it.surname} ${it.name} ${it.patronymic}" }

        // Устанавливаем адаптер для Spinner
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, // Разметка для элемента списка
            doctorNames
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.doctorSpinner.adapter = adapter
    }
    }



