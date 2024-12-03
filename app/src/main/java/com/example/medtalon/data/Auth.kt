package com.example.medtalon.data

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Auth {
    private val auth = Firebase.auth

    fun registerUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, "Пользователь успешно зарегистрирован!")
                } else {
                    Log.d("MyLogs", "Error")
                    onComplete(false, task.exception?.message)
                }
            }
    }

    fun loginUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, "Вход выполнен успешно!")
                    auth.currentUser?.let { Log.d("CurrentUser", it.uid) }
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    fun getCurrentUserId(): String{
        return auth.currentUser?.uid ?: "No Current User"
    }
}