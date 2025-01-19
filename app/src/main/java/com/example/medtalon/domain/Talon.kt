package com.example.medtalon.domain

data class Talon(
    val date: String,
    val doctor: String,
    val polyclinic: String,
    val time: String,
    val userId: String
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        ""
    )
}
