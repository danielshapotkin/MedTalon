package com.example.medtalon.domain

data class User(
    val name: String,
    val surname: String,
    val login: String,
    val patronymic: String,
    val polyclinic: String,
    val password: String
) {

}
