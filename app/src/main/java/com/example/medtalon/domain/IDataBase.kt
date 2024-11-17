package com.example.medtalon.domain

interface IDataBase {
    fun searchDoctor(query: String, onComplete: (Boolean, List<Doctor>?, String?) -> Unit)
    fun setTalon(date: String, doctor: String, polyclinic: String, time: String, onComplete: (Boolean, String?) -> Unit)
}