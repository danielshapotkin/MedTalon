package com.example.medtalon.domain

data class Doctor(
    val name: String,
    val patronymic: String,
    val qualification: String,
    val surname: String
) {
    constructor() : this("", "", "", "")
}
