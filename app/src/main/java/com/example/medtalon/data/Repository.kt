package com.example.medtalon.data

import android.content.Context
import com.example.medtalon.domain.IRepository
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

    override suspend fun searchDoctor(name: String): String {
        val resultText = StringBuilder()
        return suspendCoroutine { continuation ->
            dataBase.searchDoctor(name) { success, data, error ->
                if (success && data != null) {
                    data.forEach { doctor ->
                        resultText.append("Доктор: ${doctor.surname} ${doctor.name}\n ${doctor.patronymic}\n")
                        resultText.append("Специализация: ${doctor.qualification}\n")
                        resultText.append("-------------------------------------------------\n")
                    }
                    continuation.resume(resultText.toString())
                } else {
                    continuation.resume("Ошибка: $error")
                }
            }
        }
    }

    override suspend fun setTalon(date: String, doctor: String, polyclinic: String, time: String, onComplete: (Boolean, String?) -> Unit) {
            dataBase.setTalon(date, doctor, polyclinic, time, onComplete)
    }




}
