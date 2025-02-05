package com.example.medtalon.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.medtalon.data.DataBase
import com.example.medtalon.domain.Doctor
import com.example.medtalon.presentation.HomeViewModel
import com.example.test2.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.coroutineContext

class DoctorsAdapter(context: Context, private val doctors: List<Doctor>) :
    ArrayAdapter<Doctor>(context, 0, doctors) {
    val dataBase: DataBase = DataBase.getInstance()
    val homeViewModel: HomeViewModel = HomeViewModel.getInstance()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =
            convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.doctor_list_item, parent, false)
        val doctor = doctors[position]

        val surNameTextView = view.findViewById<TextView>(R.id.polyclinic_name)
        surNameTextView.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val commentList = withContext(Dispatchers.IO) {
                    dataBase.loadComments(doctor.id)
                }
                val builder = AlertDialog.Builder(context)
                    .setTitle("Комментарии")
                    .setItems(commentList.toTypedArray(), null)
                    .setPositiveButton("ОК", null)
                val dialog = builder.create()
                dialog.setOnShowListener {
                    val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    positiveButton.setTextColor(context.resources.getColor(android.R.color.black))  // Черный цвет
                }
                dialog.show()
            }
        }

        val nameTextView = view.findViewById<TextView>(R.id.polyclinic_worktime)
        val patronymicTextView = view.findViewById<TextView>(R.id.polyclinic_email)
        val qualificationTextView = view.findViewById<TextView>(R.id.polyclinic_adress)
        val setCommentButton = view.findViewById<Button>(R.id.getTalon_button)
        setCommentButton.setText("Оставить комментарий")
        setCommentButton.setOnClickListener {
            showCommentDialog(doctorId = doctor.id)
        }

        surNameTextView.text = doctor.surname
        nameTextView.text = doctor.name
        patronymicTextView.text = doctor.patronymic
        qualificationTextView.text = doctor.qualification



        return view
    }

    private fun showCommentDialog(doctorId: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Оставить комментарий")

        val input = EditText(context)
        input.hint = "Введите комментарий"
        builder.setView(input)

        builder.setPositiveButton("Отправить") { _, _ ->
            val commentText = input.text.toString().trim()
            if (commentText.isNotEmpty()) {
                dataBase.saveComment(doctorId, commentText)
            } else {
                Toast.makeText(context, "Комментарий не может быть пустым", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        builder.setNegativeButton("Отмена", null)

        val dialog = builder.create()

        // Изменение цвета позитивной кнопки
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(context.resources.getColor(android.R.color.black))  // Черный цвет
        }

        dialog.setOnShowListener {
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setTextColor(context.resources.getColor(android.R.color.black))  // Черный цвет
        }

        dialog.show()
    }


}
