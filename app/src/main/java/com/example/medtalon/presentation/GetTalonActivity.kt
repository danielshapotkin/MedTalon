package com.example.medtalon.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.medtalon.data.DataBase
import com.example.medtalon.domain.Doctor
import com.example.test2.R
import com.example.test2.databinding.ActivityGetTalonBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.common.reflect.TypeToken
import com.google.gson.Gson


class GetTalonActivity : AppCompatActivity() {
    private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()
    private lateinit var binding: ActivityGetTalonBinding
    private val dataBase: DataBase = DataBase.getInstance()
    private var date = ""
    private var time = ""

    private var specializations = listOf(
        "Терапевт",
        "Хирург",
        "Кардиолог",
        "Педиатр",
        "Невролог"
    )

    private var doctorTalons = mutableMapOf<String, MutableList<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetTalonBinding.inflate(layoutInflater)
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

        loadAndApdateupdateDoctorTalonsDate()


        if (homeViewModel.isLogin) {
            setupUI()
        }
    }

    private fun setupUI() {

        binding.getTalonButton.setOnClickListener {
            val doctor = binding.doctorSpinner.selectedItem.toString()
            val talonDate = date
            val talonTime = time

            if (date.isNotEmpty() && talonTime.isNotEmpty()) {
                dataBase.setTalon(
                    date = talonDate,
                    time = talonTime,
                    doctor = doctor,
                    polyclinic = homeViewModel.selectedPolyclinic.value.toString(),
                    userId = homeViewModel.currentUser
                ) { isSuccess, error ->
                    if (isSuccess) {
                        sendAppointmentNotification(doctor, date, time)
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Заказ талона")
                        builder.setMessage("Вы записаны на прием $talonDate в $talonTime \n Врач: $doctor")
                        builder.setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                            saveDoctorTalons()
                            finish()
                        }
                        builder.setNegativeButton("Отмена") { dialog, _ ->
                            dialog.dismiss()
                        }
                        builder.show()
                    } else {
                        Toast.makeText(
                            this,
                            "Ошибка заказа талона: $error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    "Выберите дату и время талона",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        setupSpecializationsSpinner()
        setupDoctorsSpinner()
        setupTalonsSpinner()
    }

    private fun setupSpecializationsSpinner() {
        when (homeViewModel.selectedPolyclinic.value){
            "Поликлиника №2" -> specializations= listOf("Терапевт", "Хирург", "Кардиолог")
            "Поликлиника №7" -> specializations= listOf("Невролог", "Хирург", "Кардиолог")
            "Поликлиника №4" -> specializations= listOf("Кардиолог", "Педиатр", "Невролог")
        }
        val specializationAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            specializations
        )
        specializationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.specializationSpinner.adapter = specializationAdapter

        binding.specializationSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedSpecialization = parent?.getItemAtPosition(position).toString()
                    filterDoctorsBySpecialization(selectedSpecialization)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }

    private fun setupDoctorsSpinner() {
        binding.doctorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedDoctor = parent?.getItemAtPosition(position).toString()
                updateTalonsSpinnerForDoctor(selectedDoctor)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupTalonsSpinner() {
        binding.talonsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedTalon = parent?.getItemAtPosition(position).toString()
                val parts = selectedTalon.split(" ")
                date = parts[0]
                time = parts[1]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun filterDoctorsBySpecialization(qualification: String) {
        dataBase.getDoctors { success, doctors, error ->
            if (success && doctors != null) {
                println(doctors)
                val filteredDoctors = doctors.filter { it.qualification == qualification }
                updateDoctorsSpinner(filteredDoctors)
            } else {
                Toast.makeText(this, "Ошибка загрузки врачей: $error", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateDoctorsSpinner(doctors: List<Doctor>) {
        val doctorNames = doctors.map { "${it.surname} ${it.name} ${it.patronymic}" }
        val doctorAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            doctorNames
        )
        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.doctorSpinner.adapter = doctorAdapter
    }

    private fun updateTalonsSpinnerForDoctor(doctor: String) {
        val talons = doctorTalons[doctor]?.toMutableList() ?: mutableListOf()
        // Create a custom onItemSelectedListener to handle talon selection
        val talonAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            talons
        )
        talonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.talonsSpinner.adapter = talonAdapter

        binding.talonsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedTalon = parent?.getItemAtPosition(position).toString()
                val parts = selectedTalon.split(" ")
                date = parts[0]
                time = parts[1]

                // Remove the selected talon from the list
                doctorTalons[doctor]?.remove(selectedTalon)

                // Update the spinner to reflect the change
                talonAdapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    fun saveDoctorTalons() {
        val sharedPreferences = getSharedPreferences("doctor_talons", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Преобразуем коллекцию doctorTalons в строку (например, JSON)
        val doctorTalonsJson = Gson().toJson(doctorTalons)
        editor.putString("doctorTalons", doctorTalonsJson)
        editor.apply()
    }

    fun loadDoctorTalons(): MutableMap<String, MutableList<String>> {
        val sharedPreferences = getSharedPreferences("doctor_talons", MODE_PRIVATE)
        val doctorTalonsJson = sharedPreferences.getString("doctorTalons", null)

        return if (doctorTalonsJson != null) {
            // Преобразуем строку обратно в объект
            Gson().fromJson(
                doctorTalonsJson,
                object : TypeToken<MutableMap<String, MutableList<String>>>() {}.type
            )
        } else {
            mutableMapOf() // Если данных нет, возвращаем пустую коллекцию
        }
    }

    fun loadAndApdateupdateDoctorTalonsDate() {
        // Загружаем текущие данные
        doctorTalons = loadDoctorTalons()

        // Изменяем дату талона для всех врачей
        doctorTalons.forEach { (doctor, talons) ->
            doctorTalons[doctor] = talons.map { talon ->
                talon.replace("27.02.2025", "28.02.2025") // Замена даты
            }.toMutableList()
        }

        // Сохраняем обновлённые данные
        saveDoctorTalons()
    }

    private fun sendAppointmentNotification(doctor: String, date: String, time: String) {
        val channelId = "appointment_channel"
        val notificationId = 1

        // Создание канала уведомлений для Android 8.0+
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Запись на прием",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Интент для открытия приложения по клику на уведомление
        val intent = Intent(this, ProfileActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Создание уведомления
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.pills_icon) // Иконка уведомления
            .setContentTitle("Вы записаны на прием")
            .setContentText("Дата: $date, Время: $time, Врач: $doctor")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        // Отправка уведомления
        notificationManager.notify(notificationId, notification)
    }
}




