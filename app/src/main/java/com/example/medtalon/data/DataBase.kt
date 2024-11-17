package com.example.medtalon.data

import Polyclinic
import com.example.medtalon.domain.Doctor
import com.example.medtalon.domain.IDataBase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class DataBase private constructor(): IDataBase{

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    companion object {
        @Volatile
        private var INSTANCE: DataBase? = null

        fun getInstance(): DataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = DataBase()
                INSTANCE = instance
                instance
            }
        }
    }

    /**
     * Поиск доктора по имени в коллекции "Doctors".
     * @param name Имя доктора.
     * @param onComplete Callback с результатом операции.
     */
    override fun searchDoctor(
        query: String,
        onComplete: (Boolean, List<Doctor>?, String?) -> Unit
    ) {
        firestore.collection("Doctors")
            .whereEqualTo("surname", query)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Десериализация документов в объекты Doctor
                    val doctors = querySnapshot.documents.map { document ->
                        document.toObject(Doctor::class.java)
                    }.filterNotNull()  // Отфильтровываем null-значения

                    onComplete(true, doctors, null)
                } else {
                    firestore.collection("Doctors")
                        .whereEqualTo("name", query)
                        .get()
                        .addOnSuccessListener {
                            if (!querySnapshot.isEmpty) {
                                val doctors = querySnapshot.documents.map { document ->
                                    document.toObject(Doctor::class.java)
                                }.filterNotNull()

                                onComplete(true, doctors, null)
                            } else {
                                firestore.collection("Doctors")
                                    .whereEqualTo("patronymic", query)
                                    .get()
                                    .addOnSuccessListener { querySnapshot ->
                                        if (!querySnapshot.isEmpty) {
                                            val doctors = querySnapshot.documents.map { document ->
                                                document.toObject(Doctor::class.java)
                                            }.filterNotNull()
                                            onComplete(true, doctors, null)
                                        } else {
                                            onComplete(
                                                false,
                                                null,
                                                "Доктор с Именем, Фамилией или Отчеством $query не найден"
                                            )
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        onComplete(false, null, exception.message)
                                    }
                            }
                        }
                        .addOnFailureListener { exception ->
                            onComplete(false, null, exception.message)
                        }
                }
            }
            .addOnFailureListener { exception ->
                onComplete(false, null, exception.message)
            }
    }

       override fun setTalon(date: String, doctor: String, polyclinic: String, time: String, onComplete: (Boolean, String?) -> Unit) {
            // Создаем объект для записи в Firestore
            val talon = hashMapOf(
                "date" to date,
                "doctor" to doctor,
                "polyclinic" to polyclinic,
                "time" to time
            )
            firestore.collection("Talons")
                .add(talon)
                .addOnSuccessListener {
                    onComplete(true, null) // Операция успешна
                }
                .addOnFailureListener { exception ->
                    onComplete(false, exception.message) // Ошибка при записи
                }
        }

     fun getPolyclinics(onComplete: (Boolean, List<Polyclinic>?, String?) -> Unit) {
        firestore.collection("Polyclinics")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val polyclinics = querySnapshot.documents.map { document ->
                        document.toObject(Polyclinic::class.java)
                    }.filterNotNull() // Отфильтровываем null-значения

                    onComplete(true, polyclinics, null)
                } else {
                    onComplete(false, null, "Список поликлиник пуст")
                }
            }
            .addOnFailureListener { exception ->
                onComplete(false, null, exception.message)
            }
    }


        fun getDoctors(onComplete: (Boolean, List<Doctor>?, String?) -> Unit) {
            firestore.collection("Doctors")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val doctors = querySnapshot.documents.map { document ->
                            document.toObject(Doctor::class.java)
                        }.filterNotNull() // Отфильтровываем null-значения

                        onComplete(true, doctors, null)
                    } else {
                        onComplete(false, null, "Список поликлиник пуст")
                    }
                }
                .addOnFailureListener { exception ->
                    onComplete(false, null, exception.message)
                }
        }
}

