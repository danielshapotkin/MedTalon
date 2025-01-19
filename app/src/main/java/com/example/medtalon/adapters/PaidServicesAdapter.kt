package com.example.medtalon.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.DEFAULT_ARGS_KEY
import com.example.medtalon.data.DataBase
import com.example.medtalon.domain.PaidService
import com.example.medtalon.presentation.GetTalonActivity
import com.example.medtalon.presentation.HomeViewModel
import com.example.test2.R

class PaidServicesAdapter(context: Context, private val paidServices: List<PaidService>) :
    ArrayAdapter<PaidService>(context, 0, paidServices) {
        val dataBase: DataBase = DataBase.getInstance()
        val homeViewModel: HomeViewModel = HomeViewModel.getInstance()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val paidService = paidServices[position]

        val nameTextView = view.findViewById<TextView>(R.id.polyclinic_name)
        val priceTextView = view.findViewById<TextView>(R.id.polyclinic_worktime)
        val emailTextView = view.findViewById<TextView>(R.id.polyclinic_email)
        val addressTextView = view.findViewById<TextView>(R.id.polyclinic_adress)
        val timeTextView = view.findViewById<TextView>(R.id.polyclinic_url)
        val getTalonButton = view.findViewById<Button>(R.id.getTalon_button)
        getTalonButton.setOnClickListener{
            dataBase.setTalon(date = paidService.date, time = paidService.time, doctor = paidService.name, polyclinic = paidService.polyclinic, userId = homeViewModel.currentUser){isSuccess, error->
            }
            Toast.makeText(context, "Талон успешно заказан", Toast.LENGTH_LONG).show()
        }

        nameTextView.text = paidService.name
        priceTextView.text = paidService.price
        emailTextView.text = paidService.polyclinic
        addressTextView.text = paidService.date
        timeTextView.text = paidService.time


        return view
    }
}
