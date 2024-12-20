package com.example.medtalon.presentation


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.MenuInflater
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.medtalon.data.Repository
import com.example.medtalon.domain.IRepository
import com.example.test2.R
import com.example.test2.databinding.ActivitySearchResultBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

@Suppress("NAME_SHADOWING")
class SearchResultActivity : AppCompatActivity(

) {

    private lateinit var binding: ActivitySearchResultBinding
    private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()
    private val repository: IRepository = Repository.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val query = intent.getStringExtra("QUERY")
        lifecycleScope.launch {
            if (query != null) {
                val result = withContext(Dispatchers.IO) {
                    repository.search(query)
                }
                binding.searchResultsTextView.text = result
            }
        }

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
                }, year, month, day
            )
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

        binding.makeAnAppointmentButton.setOnClickListener {
            val doctor = binding.searchResultsTextView.text.toString()
            val time = binding.selectedTime.text.toString()
            val date = binding.selectedDate.text.toString()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Заказ талона")
            builder.setMessage("Вы записаны на прием $date в $time \n Варач $doctor")
            builder.setPositiveButton("OK") { dialog, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    repository.setTalon(
                        date = date,
                        doctor = doctor,
                        polyclinic = "",
                        time = time
                    ) { success, message ->
                        if (success) {
                            println("Талон успешно добавлен!")
                        } else {
                            println("Ошибка при добавлении талона: $message")
                        }
                    }
                }
                dialog.dismiss()
                finish()
            }
            builder.setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }


    }
}
