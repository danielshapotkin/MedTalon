    package com.example.test2

    import android.os.Bundle
    import android.util.Log
    import android.widget.Button
    import android.widget.TextView
    import androidx.appcompat.app.AppCompatActivity
    import com.google.firebase.firestore.ktx.firestore
    import com.google.firebase.ktx.Firebase

    class MainActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            val db = Firebase.firestore


            val list = mutableListOf<Book>()

            db.collection("hi").document().set(
                Book(
                    "MyBook",
                    "About...",
                    "12",
                    "Fantasy",
                    "URL"
                )
            )


            db.collection("hi")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        list.addAll(task.result.toObjects(Book::class.java))
                        Log.d("Firestore", "Data: $list")
                        for (book in list) {
                            Log.d("Str", book.name)
                           // tw.text = tw.text.toString() + book.name
                        }
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.exception)
                    }
                }
        }
    }

