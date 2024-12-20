package com.example.medtalon.presentation


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.test2.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val homeViewModel = HomeViewModel.getInstance()
    private val refUser = FirebaseFirestore.getInstance()
        .collection("Users")
        .document(homeViewModel.currentUser)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nameTextView.text = homeViewModel.currentUser

        binding.polyclinicEditText.addTextChangedListener { editable ->
            binding.polyclinicEditText.addTextChangedListener { editable ->
                val newPolyclinic = editable.toString()
                    refUser.update("polyclinic", newPolyclinic)
            }

            binding.nameEditText.addTextChangedListener { editable ->
                val newName = editable.toString()
                refUser.update("name", newName)
            }

            binding.nameEditText.addTextChangedListener { editable ->
                val newSurname = editable.toString()
                refUser.update("surname", newSurname)
            }

            binding.nameEditText.addTextChangedListener { editable ->
                val newPatronymic = editable.toString()
                refUser.update("patronymic", newPatronymic)
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}
