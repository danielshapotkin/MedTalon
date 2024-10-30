package com.example.medtalon.domain

interface IRepository {
    suspend fun getToken(authCode: String): String
    suspend fun getUsername():String
}
