package com.example.medtalon.domain

data class PaidService(
    val name: String,
    val price: String
)
{
    constructor() : this(
        "",
        ""
    )
}