package com.example.medtalon.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.medtalon.adapters.AnalysisAdapter
import com.example.medtalon.data.DataBase
import com.example.medtalon.domain.Analysis
import com.example.test2.R
import com.example.test2.databinding.ActivityChoiceAnalysisBinding
import com.example.test2.databinding.ActivityMyAnalysisBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

class MyAnalysisActivity : AppCompatActivity() {
    private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()
    private val dataBase: DataBase = DataBase.getInstance()
    private lateinit var binding: ActivityMyAnalysisBinding
    private lateinit var analysis: List<Analysis>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            val analysis = listOf(dataBase.getAnalysis(homeViewModel.currentUser))
            binding.analysisListView.adapter = AnalysisAdapter(this@MyAnalysisActivity, listOf(Analysis(
                analysis[0].toString(), "")) )
        }



        binding.searchButton.setOnClickListener{
            filterAnalysis(binding.searchEditText.text.toString())
        }

        binding.backButton.setOnClickListener{
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

    private fun filterAnalysis(query: String) {
        val filteredPolyclinics =
            analysis.sortedByDescending { it.name.contains(query, ignoreCase = true) }
        updateListView(filteredPolyclinics)
    }

    private fun updateListView(analysis: List<Analysis>) {
        val adapter = AnalysisAdapter(this, analysis)
        binding.analysisListView.adapter = adapter
    }
}