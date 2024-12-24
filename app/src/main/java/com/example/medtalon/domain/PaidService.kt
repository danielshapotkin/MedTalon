package com.example.medtalon.domain

data class PaidService(
    val name: String,
    val price: String,
    val polyclinic: String,
    val date: String,
    val time: String
)
{
    constructor() : this(
        "",
        "",
        "",
        "",
        ""
    )
}