package com.example.medtalon

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.medtalon.domain.PaidService
import com.example.test2.R

class PaidServicesAdapter(context: Context, private val paidServices: List<PaidService>) :
    ArrayAdapter<PaidService>(context, 0, paidServices) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val paidService = paidServices[position]

        val nameTextView = view.findViewById<TextView>(R.id.polyclinic_name)
        val priceTextView = view.findViewById<TextView>(R.id.polyclinic_worktime)
        val emailTextView = view.findViewById<TextView>(R.id.polyclinic_email)
        val addressTextView = view.findViewById<TextView>(R.id.polyclinic_adress)
        val urlTextView = view.findViewById<TextView>(R.id.polyclinic_url)


        nameTextView.text = paidService.name
        priceTextView.text = paidService.price
        emailTextView.text = ""
        addressTextView.text = ""
        urlTextView.text = ""



        return view
    }
}
