package com.example.medtalon.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuInflater
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.example.test2.R
import com.example.test2.databinding.ActivityCallDoctorBinding
import com.example.test2.databinding.ActivityGetTalonBinding

class CallDoctorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCallDoctorBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.backButton.setOnClickListener {
            finish()
        }

        binding.profileButton.setOnClickListener{
            val popup = PopupMenu(this, it)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.profile_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.view_profile -> {

                        true
                    }
                    R.id.logout -> {

                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }


binding.hiddenPolyclinicInfoTextview.setOnClickListener{
    val url = "https://26poliklinika.by"
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    startActivity(intent)
}


    }
}