package com.example.medtalon.adapters

import com.example.medtalon.data.DataBase
import com.example.medtalon.domain.Analysis
import Polyclinic
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import com.example.medtalon.presentation.GetTalonActivity
import com.example.medtalon.presentation.HomeViewModel
import com.example.test2.R

class AnalysisAdapter(context: Context, private val analysis: List<Analysis>) :
    ArrayAdapter<Analysis>(context, 0, analysis) {
    private val dataBase: DataBase = DataBase.getInstance()
    private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val analysis = analysis[position]

        val nameTextView = view.findViewById<TextView>(R.id.polyclinic_name)
        val worktimeTextView = view.findViewById<TextView>(R.id.polyclinic_worktime)
        val emailTextView = view.findViewById<TextView>(R.id.polyclinic_email)
        val addressTextView = view.findViewById<TextView>(R.id.polyclinic_adress)
        val urlTextView = view.findViewById<TextView>(R.id.polyclinic_url)
        val getTalonButton = view.findViewById<Button>(R.id.getTalon_button)

        nameTextView.text = analysis.name
        worktimeTextView.text = ""
        emailTextView.text = analysis.price
        addressTextView.text = ""
        urlTextView.text = ""

        getTalonButton.text = "Заказано"

        getTalonButton.setOnClickListener{
            dataBase.setAnalysis(homeViewModel.currentUser, analysis.name)
            Toast.makeText(context, "Анализ добавлен в профиль", Toast.LENGTH_LONG).show()
        }

        return view
    }
}
