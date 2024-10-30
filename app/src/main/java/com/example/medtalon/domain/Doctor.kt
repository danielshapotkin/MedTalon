package com.example.medtalon.domain

data class Doctor(
    val name: String,
    val patronymic: String,
    val surname: String,
    val qualification: String
) {
    constructor() : this("", "", "", "")
}
