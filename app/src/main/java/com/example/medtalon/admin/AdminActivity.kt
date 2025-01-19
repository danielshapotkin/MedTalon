package com.example.medtalon.admin

import UsersAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medtalon.data.DataBase
import com.example.medtalon.presentation.HomeViewModel
import com.example.medtalon.presentation.ProfileActivity
import com.example.medtalon.presentation.SettingsActivity
import com.example.test2.R
import com.example.test2.databinding.ActivityAdminBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private val dataBase = DataBase.getInstance()
    private val homeViewModel:HomeViewModel = HomeViewModel.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            val users = withContext(Dispatchers.IO) {
                dataBase.getAllUsers().toMutableList()
            }
            val adapter = UsersAdapter(this@AdminActivity, users, lifecycleScope)
            binding.recyclerView.adapter = adapter
        }


        binding.backButton.setOnClickListener {
            finish()
        }

        binding.profileButton.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
            view.findViewById<Button>(R.id.view_profile_button).setOnClickListener {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                bottomSheetDialog.dismiss()
            }

            view.findViewById<Button>(R.id.settings_button).setOnClickListener {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                bottomSheetDialog.dismiss()
            }

            view.findViewById<Button>(R.id.logout_button).setOnClickListener {
                homeViewModel.logout()
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        }

    }
}
