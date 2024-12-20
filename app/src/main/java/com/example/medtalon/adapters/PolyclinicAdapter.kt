package com.example.medtalon.adapters

import Polyclinic
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import com.example.medtalon.presentation.GetTalonActivity
import com.example.test2.R

class PolyclinicAdapter(context: Context, private val polyclinics: List<Polyclinic>) :
    ArrayAdapter<Polyclinic>(context, 0, polyclinics) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val polyclinic = polyclinics[position]

        val nameTextView = view.findViewById<TextView>(R.id.polyclinic_name)
        val worktimeTextView = view.findViewById<TextView>(R.id.polyclinic_worktime)
        val emailTextView = view.findViewById<TextView>(R.id.polyclinic_email)
        val addressTextView = view.findViewById<TextView>(R.id.polyclinic_adress)
        val urlTextView = view.findViewById<TextView>(R.id.polyclinic_url)
        val getTalonButton = view.findViewById<Button>(R.id.getTalon_button)

        nameTextView.text = polyclinic.name
        worktimeTextView.text = polyclinic.worktime
        emailTextView.text = polyclinic.email
        addressTextView.text = polyclinic.address
        urlTextView.text = polyclinic.url

        getTalonButton.setOnClickListener{
            val intent = Intent(context, GetTalonActivity::class.java)
            context.startActivity(intent)
        }

        return view
    }
}
