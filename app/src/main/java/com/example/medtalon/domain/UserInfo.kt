package com.example.medtalon.domain

data class UserInfo(
    val login: String,
    val name: String,
    val password: String,
    val patronymic: String,
    val polyclinic: String,
    val surname: String
)