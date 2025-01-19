package com.example.medtalon.domain

data class Doctor(
    val surname: String,
    val name: String,
    val patronymic: String,
    val qualification: String,
    val talons: List<String>
) {
    constructor() : this("", "", "", "", listOf(""))
}
