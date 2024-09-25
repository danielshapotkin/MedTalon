package com.example.test2

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Получаем экземпляр Firestore
        val db = Firebase.firestore

        // Записываем данные в коллекцию "hi"
        db.collection("hi").document().set(mapOf("Name" to "MyBook"))

        // Чтение всех документов из коллекции "hi"
        db.collection("hi")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("MainActivity", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("MainActivity", "Error getting documents: ", exception)
            }
    }
}
