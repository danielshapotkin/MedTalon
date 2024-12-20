package com.example.medtalon.domain

interface IRepository {
    suspend fun search(name:String): String
    suspend fun setTalon(date: String, doctor: String, polyclinic: String, time: String, onComplete: (Boolean, String?) -> Unit)

}
