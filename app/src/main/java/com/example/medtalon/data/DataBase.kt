package com.example.medtalon.data

import Polyclinic
import android.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.example.medtalon.data.App.Companion.context
import com.example.medtalon.domain.Analysis
import com.example.medtalon.domain.Doctor
import com.example.medtalon.domain.IDataBase
import com.example.medtalon.domain.PaidService
import com.example.medtalon.domain.User
import com.example.medtalon.domain.UserInfo
import com.example.medtalon.presentation.HomeViewModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        userId: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val talon = hashMapOf(
            "date" to date,
            "doctor" to doctor,
            "polyclinic" to polyclinic,
            "time" to time,
            "userId" to userId
        )
        firestore.collection("Talons").document().set(talon)
            .addOnSuccessListener {
                onComplete(true, null)
            }
            .addOnFailureListener { exception ->
                onComplete(false, exception.message)
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

    fun getUserClinic(login: String, onResult: (polyclinic: String?) -> Unit) {
        val userCollection = firestore.collection("Users").document(login)
        userCollection.get().addOnSuccessListener { document ->
            val polyclinic = document.getString("polyclinic")
            onResult(polyclinic)
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
                    val doctors = querySnapshot.documents.mapNotNull { document ->
                        val doctor = document.toObject(Doctor::class.java)
                        doctor?.copy(id = document.id) // Добавляем id из Firestore
                    }

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

    fun setUser(user: User) {
        val userId = user.login
        val user = hashMapOf(
            "name" to user.name,
            "polyclinic" to user.polyclinic,
            "login" to user.login,
            "password" to user.password
        )
        firestore.collection("Users").document(userId).set(user)
    }


    suspend fun getUserByLogin(login: String): User? {
        val documentSnapshot = firestore
            .collection("Users")
            .document(login)
            .get()
            .await() // Используем await для работы с корутинами
        return documentSnapshot.toObject(User::class.java)
    }

    fun setAnalysis(currentUser: String, nameOfAnalysis: String) {
        firestore
            .collection("Users")
            .document(currentUser)
            .update("analysis", FieldValue.arrayUnion(nameOfAnalysis))
    }

    suspend fun getAnalysis(currentUser: String): List<String>? {
        val documentSnapshot = firestore
            .collection("Users")
            .document(currentUser)
            .get()
            .await()
        return documentSnapshot.get("analysis") as? List<String>
    }

    suspend fun deleteUserByLogin(login: String): Boolean {
        return try {
            firestore
                .collection("Users")
                .document(login)
                .delete()
                .await() // Используем await для выполнения операции в корутине
            true // Возвращаем true, если удаление прошло успешно
        } catch (e: Exception) {
            e.printStackTrace()
            false // Возвращаем false в случае ошибки
        }
    }

    suspend fun getAllUsers(): List<User> {
        return try {
            val querySnapshot = firestore
                .collection("Users")
                .get()
                .await() // Ожидаем завершения асинхронной операции
            querySnapshot.documents.mapNotNull { document ->
                document.toObject(User::class.java)
            } // Преобразуем документы в объекты User
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Возвращаем пустой список в случае ошибки
        }
    }

    fun saveComment(doctorId: String, comment: String) {
        val commentsRef = firestore.collection("Doctors").document(doctorId).collection("comments")
        val commentData = hashMapOf(
            "text" to comment,
            "timestamp" to SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
            ).format(Date())
        )

        commentsRef.add(commentData)
            .addOnSuccessListener {
                Toast.makeText(context, "Комментарий добавлен", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    suspend fun loadComments(doctorId: String): List<String> {
        return try {
            val snapshot =
                firestore.collection("Doctors").document(doctorId).collection("comments").get()
                    .await()
            snapshot.documents.mapNotNull { it.getString("text") }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

