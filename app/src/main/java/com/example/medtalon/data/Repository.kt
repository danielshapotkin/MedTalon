package com.example.medtalon.data

import android.content.Context
import android.util.Log
import com.example.medtalon.domain.IRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Repository(
    private val context: Context,
) : IRepository {
    private val dataBase: DataBase = DataBase.getInstance()

    companion object {
        @Volatile
        private var instance: IRepository? = null

        fun getInstance(context: Context): IRepository {

            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = Repository(context)
                    }
                }
            }
            return instance!!
        }
    }

    override suspend fun setTalon(
        date: String,
        doctor: String,
        polyclinic: String,
        time: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        dataBase.setTalon(date, doctor, polyclinic, time, onComplete)
    }

    override suspend fun search(query: String): String {
        return try {
            val (polyclinics, paidServices, doctors) = dataBase.searchInCollections(query)

            val result = buildString {
                polyclinics.forEach { append("- ${it.name} ${it.address}\n ${it.worktime}\n ${it.url}\n ${it.email}\n") }

                paidServices.forEach { append("- ${it.name}\n ${it.price}\n") }

                doctors.forEach { append("- ${it.name} ${it.surname} ${it.patronymic}\n ${it.qualification}") }
            }

            result
        } catch (e: Exception) {
            Log.e("Repository", "Error during search: ${e.message}", e)
            "Error: ${e.message}"
        }
    }



}
