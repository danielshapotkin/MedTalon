package com.example.test2

data class Book(
    val name: String,
    val description: String,
    val price: String,
    val category: String,
    val imgUrl: String,
) {
    constructor() : this("", "", "", "", "")
}