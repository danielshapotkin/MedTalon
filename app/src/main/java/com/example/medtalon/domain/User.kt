package com.example.medtalon.domain

data class User(
    val name: String,
    val login: String,
    val password: String,
    val polyclinic: String
) {
    constructor() : this("", "", "","")
}
