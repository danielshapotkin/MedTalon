package com.example.medtalon

import Polyclinic
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.test2.R

class PolyclinicAdapter(context: Context, private val polyclinics: List<Polyclinic>) :
    ArrayAdapter<Polyclinic>(context, 0, polyclinics) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val polyclinic = polyclinics[position]

        val nameTextView = view.findViewById<TextView>(R.id.polyclinic_name)
        val addressTextView = view.findViewById<TextView>(R.id.polyclinic_address)
        val worktimeTextView = view.findViewById<TextView>(R.id.polyclinic_worktime)
        val emailTextView = view.findViewById<TextView>(R.id.polyclinic_email)
        val urlTextView = view.findViewById<TextView>(R.id.polyclinic_url)

        nameTextView.text = polyclinic.name
        worktimeTextView.text = polyclinic.workTime
        emailTextView.text = polyclinic.email
        addressTextView.text = polyclinic.adress
        urlTextView.text = polyclinic.url



        return view
    }
}
