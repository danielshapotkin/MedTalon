package com.example.medtalon.domain

data class User(
    val name: String,
    val password: String
) {
    constructor() : this(
        "",
        ""
    )
}
