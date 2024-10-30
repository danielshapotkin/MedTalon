    package com.example.medtalon.presentation

    import android.os.Bundle
    import android.util.Log
    import androidx.appcompat.app.AppCompatActivity
    import com.example.medtalon.domain.Doctor
    import com.example.test2.R
    import com.example.test2.databinding.ActivityMainBinding
    import com.google.firebase.firestore.ktx.firestore
    import com.google.firebase.ktx.Firebase
    import com.google.firebase.storage.ktx.storage

    class MainActivity : AppCompatActivity() {
        private val fs = Firebase.firestore
        private val storage = Firebase.storage.reference
        private lateinit var binding: ActivityMainBinding


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)





            binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        // Обработка нажатия на Home
                        true
                    }
                    R.id.navigation_analysis -> {
                        // Обработка нажатия на Search
                        true
                    }
                    R.id.navigation_pills -> {
                        // Обработка нажатия на Profile
                        true
                    }
                    R.id.navigation_blog -> {
                        // Обработка нажатия на Profile
                        true
                    }
                    else -> false
                }
            }








            val list = mutableListOf<Doctor>()
            fs.collection("Doctors").document().set(
                Doctor(
                    "Daniel",
                    "Pavlovich",
                    "Shapotkin",
                    "Dantist"
                )
            )


            fs.collection("Doctors")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        list.addAll(task.result.toObjects(Doctor::class.java))
                        for (doctor in list) {
                            binding
                        }
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.exception)
                    }
                }
        }
    }

