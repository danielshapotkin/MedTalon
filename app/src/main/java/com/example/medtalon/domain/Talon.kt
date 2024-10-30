package com.example.medtalon.domain

data class Talon(
    val doctor: String,
    val dateTime: String,
    val service: String,
    val price: Float
) {
    constructor() : this(
        "",
        "",
        "",
        0F
    )
}
