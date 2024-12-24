package com.example.medtalon.presentation


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
                homeViewModel.setSelectedPolyclinic(newPolyclinic)
            }

            binding.nameEditText.addTextChangedListener { editable ->
                val newName = editable.toString()
                refUser.update("name", newName)
                homeViewModel.setSelectedPatient(newName)
            }

            binding.backButton.setOnClickListener {
                finish()
            }
        }
    }
}
