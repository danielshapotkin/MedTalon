package com.example.medtalon.presentation


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuInflater
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.example.test2.R
import com.example.test2.databinding.ActivityCallDoctorBinding
import com.example.test2.databinding.ActivityGetTalonBinding
import com.example.test2.databinding.ActivitySearchResultBinding
import java.util.Calendar

class SearchResultActivity : AppCompatActivity(

) {

    private lateinit var binding: ActivitySearchResultBinding
    private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.profileButton.setOnClickListener {
            val popup = PopupMenu(this, it)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.profile_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.view_profile -> {
                        homeViewModel.showProfile()
                        true
                    }
                    R.id.logout -> {
                        homeViewModel.logout()
                        finish()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        binding.selectDateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                    binding.selectedDate.text = selectedDate
                }, year, month, day)
            datePickerDialog.show()
        }

        binding.selectTimeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                binding.selectedTime.text = selectedTime
            }, hour, minute, true)
            timePickerDialog.show()
        }

        binding.makeAnAppointmentButton.setOnClickListener{
            val doctor = binding.searchResultsTextView.text
            val time = binding.selectedTime
            val date = binding.selectedDate
        }

        val searchResults = intent.getStringArrayListExtra("SEARCH_RESULTS")
        binding.searchResultsTextView.text =
            searchResults?.get(0) ?: "Услуги, врача или учреждения не найдено"

    }
}