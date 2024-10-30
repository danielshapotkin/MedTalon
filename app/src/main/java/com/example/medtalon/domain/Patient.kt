package com.example.medtalon.domain

data class Patient(
    val surname: String,
    val name: String,
    val patronymic: String,
    val polyclinic: String
) {
    constructor() : this(
        "",
        "",
        "",
        ""
    )
}
