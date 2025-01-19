package com.example.medtalon.domain

import Polyclinic

interface IDataBase {
    fun setTalon(date: String, doctor: String, polyclinic: String, time: String, currentUser: String, onComplete: (Boolean, String?) -> Unit)
}