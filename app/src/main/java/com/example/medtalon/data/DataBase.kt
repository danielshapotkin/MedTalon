package com.example.medtalon.data

import Polyclinic
import android.util.Log
import com.example.medtalon.domain.Doctor
import com.example.medtalon.domain.IDataBase
import com.example.medtalon.domain.PaidService
import com.example.medtalon.domain.User
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class DataBase private constructor() : IDataBase {

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

    override fun setTalon(
        date: String,
        doctor: String,
        polyclinic: String,
        time: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
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

    fun getPaidServices(onComplete: (Boolean, List<PaidService>?, String?) -> Unit) {
        firestore.collection("PaidServices")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val paidService = querySnapshot.documents.map { document ->
                        document.toObject(PaidService::class.java)
                    }.filterNotNull() // Отфильтровываем null-значения

                    onComplete(true, paidService, null)
                } else {
                    onComplete(false, null, "Список платынх услуг пуст")
                }
            }
            .addOnFailureListener { exception ->
                onComplete(false, null, exception.message)
            }
    }

    fun linkUserToClinic(
        userId: String,
        clinicId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()

        // Обновляем документ пользователя, добавляя clinicId
        db.collection("users").document(userId)
            .update("clinicId", clinicId)
            .addOnSuccessListener {
                println("User $userId successfully linked to clinic $clinicId")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                println("Error linking user to clinic: ${exception.message}")
                onError(exception)
            }
    }

    fun getUserClinic(userId: String, onResult: (clinicName: String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("Users").document(userId)

        userRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val clinicId = document.getString("clinicId")
                if (clinicId != null) {
                    // Получаем данные поликлиники
                    db.collection("Polyclinics").document(clinicId).get()
                        .addOnSuccessListener { clinicDoc ->
                            val clinicName = clinicDoc.getString("name")
                            onResult(clinicName)
                        }
                        .addOnFailureListener { e ->
                            println("Ошибка при получении данных поликлиники: ${e.message}")
                            onResult(null)
                        }
                } else {
                    println("Пользователь не привязан к поликлинике.")
                    onResult(null)
                }
            } else {
                println("Пользователь не найден.")
                onResult(null)
            }
        }.addOnFailureListener { e ->
            println("Ошибка при получении данных пользователя: ${e.message}")
            onResult(null)
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
                    onComplete(false, null, "Список докторов пуст")
                }
            }
            .addOnFailureListener { exception ->
                onComplete(false, null, exception.message)
            }
    }

    suspend fun searchInCollections(query: String): Triple<List<Polyclinic>, List<PaidService>, List<Doctor>> {
        val db = FirebaseFirestore.getInstance()

        val polyclinicsTask = db.collection("Polyclinics")
            .whereEqualTo("name", query)
            .get()

        val paidServicesTask = db.collection("PaidServices")
            .whereEqualTo("name", query)
            .get()

        val doctorsTask = db.collection("Doctors")
            .whereEqualTo("name", query)
            .get()

        // Выполняем запросы параллельно
        val polyclinicsResult = polyclinicsTask.await()
        val paidServicesResult = paidServicesTask.await()
        val doctorsResult = doctorsTask.await()

        // Преобразуем результаты в списки объектов
        val polyclinics = polyclinicsResult.toObjects(Polyclinic::class.java)
        val paidServices = paidServicesResult.toObjects(PaidService::class.java)
        val doctors = doctorsResult.toObjects(Doctor::class.java)

        return Triple(polyclinics, paidServices, doctors)
    }

    fun setUser (user: User){
        val userId = user.login
        val user = hashMapOf(
            "name" to user.name,
            "surname" to user.surname,
            "polyclinic" to user.polyclinic,
            "patronymic" to user.patronymic,
            "login" to user.login,
            "password" to user.password
        )
        firestore.collection("Users").document(userId).set(user)
    }
}

