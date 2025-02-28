package com.example.medtalon.presentation


import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.medtalon.domain.Talon
import com.example.test2.databinding.ActivityProfileBinding
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {


    private lateinit var binding: ActivityProfileBinding
    private val homeViewModel = HomeViewModel.getInstance()
    private val refUser = FirebaseFirestore.getInstance()
        .collection("Users")
        .document(homeViewModel.currentUser)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val talonsCollection = FirebaseFirestore.getInstance().collection("Talons")

// Запрашиваем все документы, где поле "userId" соответствует текущему пользователю
        talonsCollection.whereEqualTo("userId", homeViewModel.currentUser)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Обрабатываем успешный ответ
                val talonsList = querySnapshot.documents.map { document ->
                    // Преобразуем документы в объекты талонов
                    val talon = document.toObject(Talon::class.java)
                    talon
                }
                val formattedTalons = StringBuilder()

                for (talon in talonsList) {
                    formattedTalons.append("Врач: ${talon?.doctor}\n")
                    formattedTalons.append("Дата: ${talon?.date}\n")
                    formattedTalons.append("Время: ${talon?.time}\n")
                    formattedTalons.append("-----------------------------\n")  // Добавляем разделитель между талонами
                }

                binding.myTalonsTextview.text = formattedTalons.toString()
            }
            .addOnFailureListener { exception ->
                println("Ошибка при получении талонов: ${exception.message}")
            }

        binding.nameTextView.text = homeViewModel.currentUser

        refUser.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val name = documentSnapshot.getString("name")
                    name?.let {
                        binding.nameEditText.setText(it)
                    }
                }
            }


        val polyclinics = listOf(
            "Поликлиника №1",
            "Поликлиника №2",
            "Поликлиника №3",
            "Поликлиника №4",
            "Поликлиника №5",
            "Поликлиника №6",
            "Поликлиника №7",
            "Поликлиника №8",
            "Поликлиника №9",
            "Поликлиника №10",
            "Поликлиника №11",
            "Поликлиника №12",
            "Поликлиника №13",
            "Поликлиника №14",
            "Поликлиника №15",
            "Поликлиника №16",
            "Поликлиника №17",
            "Поликлиника №18",
            "Поликлиника №19",
            "Поликлиника №20",
            "Поликлиника №21",
            "Поликлиника №22",
            "Поликлиника №23",
            "Поликлиника №24",
            "Поликлиника №25",
            "Поликлиника №26",
            "Поликлиника №27",
            "Поликлиника №28",
            "Поликлиника №29",
            "Поликлиника №30",
            "Поликлиника №31",
            "Поликлиника №32",
            "Поликлиника №33",
            "Поликлиника №34",
            "Поликлиника №35",
            "Поликлиника №36",
            "Поликлиника №37",
            "Поликлиника №38",
            "Поликлиника №39",
            "Поликлиника №40"
        )

        val polyclinicAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            polyclinics
        )
        polyclinicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.polyclinicSpinner.adapter = polyclinicAdapter

        binding.polyclinicSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedPolyclinic = parent?.getItemAtPosition(position).toString()
                    refUser.update("polyclinic", selectedPolyclinic)
                    homeViewModel.setSelectedPolyclinic(selectedPolyclinic)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        val userPolyclinic = homeViewModel.selectedPolyclinic.value
        val defaultIndex = polyclinics.indexOf(userPolyclinic)

        if (defaultIndex != -1) {
            binding.polyclinicSpinner.setSelection(defaultIndex)
        }





        binding.nameEditText.addTextChangedListener { editable ->
            val newName = editable.toString()
            refUser.update("name", newName)
            homeViewModel.setSelectedPatient(newName)
        }


        binding.backButton.setOnClickListener {
            finish()
        }
    }
}

