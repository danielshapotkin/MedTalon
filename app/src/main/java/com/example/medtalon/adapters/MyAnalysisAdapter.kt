package com.example.medtalon.adapters

import com.example.medtalon.data.DataBase
import com.example.medtalon.domain.Analysis
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.medtalon.presentation.HomeViewModel
import com.example.test2.R

class MyAnalysisAdapter(context: Context, private val analysis: List<String>) :
    ArrayAdapter<String>(context, 0, analysis) {
    private val dataBase: DataBase = DataBase.getInstance()
    private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =
            convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val analysis = analysis[position]

        val nameTextView = view.findViewById<TextView>(R.id.polyclinic_name)
        val getTalonButton = view.findViewById<Button>(R.id.getTalon_button)
        val time = view.findViewById<TextView>(R.id.polyclinic_worktime)
        val email = view.findViewById<TextView>(R.id.polyclinic_email)
        val address = view.findViewById<TextView>(R.id.polyclinic_adress)

        nameTextView.text = analysis
        time.text = ""
        email.text = ""
        address.text = ""

        getTalonButton.text = "Заказано"

        getTalonButton.setOnClickListener {
            dataBase.setAnalysis(homeViewModel.currentUser, analysis)
            Toast.makeText(context, "Анализ добавлен в профиль", Toast.LENGTH_LONG).show()
        }

        return view
    }
}
