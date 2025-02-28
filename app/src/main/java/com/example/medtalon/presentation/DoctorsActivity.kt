package com.example.medtalon.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.medtalon.adapters.DoctorsAdapter
import com.example.medtalon.adapters.PolyclinicAdapter
import com.example.medtalon.data.DataBase
import com.example.medtalon.domain.Doctor
import com.example.test2.R

import com.example.test2.databinding.ActivityDoctorsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DoctorsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorsBinding
    private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()
    private val dataBase: DataBase = DataBase.getInstance()
    private var doctors = emptyList<Doctor>()
    private var loadingDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDoctorsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.profileButton.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
            view.findViewById<Button>(R.id.view_profile_button).setOnClickListener {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                bottomSheetDialog.dismiss()
            }

            view.findViewById<Button>(R.id.settings_button).setOnClickListener {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                bottomSheetDialog.dismiss()
            }

            view.findViewById<Button>(R.id.logout_button).setOnClickListener {
                homeViewModel.logout()
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        }

        binding.searchButton.setOnClickListener {
            filterDoctors(binding.searchEditText.text.toString())
        }

        showLoadingDialog()
        lifecycleScope.launch(Dispatchers.IO) {
            dataBase.getDoctors { success, doctors, error ->
                hideLoadingDialog()
                runOnUiThread {
                    if (success && doctors != null) {
                        this@DoctorsActivity.doctors = doctors
                        binding.doctorsListView.adapter = DoctorsAdapter(this@DoctorsActivity, doctors)
                    } else {
                        Toast.makeText(
                            this@DoctorsActivity,
                            "Ошибка загрузки врачей: $error",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun updateListView(doctors: List<Doctor>) {
        val adapter = DoctorsAdapter(this, doctors)
        binding.doctorsListView.adapter = adapter
    }

    private fun filterDoctors(query: String) {
        val filteredDoctor =
            doctors.sortedByDescending { it.surname.contains(query, ignoreCase = true)}
        updateListView(filteredDoctor)
    }

    private fun showLoadingDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.dialog_loading, null)

        builder.setView(view)
        builder.setCancelable(false) // Запрещаем закрытие диалога вручную

        loadingDialog = builder.create()
        loadingDialog?.show()
    }

    private fun hideLoadingDialog() {
        loadingDialog?.dismiss()
    }
}