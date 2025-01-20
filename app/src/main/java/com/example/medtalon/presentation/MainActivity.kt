package com.example.medtalon.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medtalon.presentation.HomeViewModel.Events
import com.example.test2.R
import com.example.test2.databinding.ActivityMainBinding
import com.example.test2.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var homeBinding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()
    private val fs = Firebase.firestore
    private val storage = Firebase.storage.reference
    private val _events = MutableLiveData<Events>()
    val events: LiveData<Events> get() = _events


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        homeBinding = FragmentHomeBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.regionButton.setOnClickListener {
            val popupMenu = PopupMenu(this, mainBinding.regionButton)
            popupMenu.menuInflater.inflate(R.menu.region_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                mainBinding.regionButton.text = item.title
                homeViewModel.setSelectedRegion(item.title.toString())
                true
            }
            popupMenu.show()
        }

        mainBinding.profileButton.setOnClickListener {
            if (homeViewModel.isLogin){
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
            } else{
                intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }



        mainBinding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val homeFragment = HomeFragment()
                    replaceFragment(homeFragment)
                    true
                }

                R.id.navigation_analysis -> {
                    val analysisFragment = AnalysisFragment()
                    replaceFragment(analysisFragment)
                    true
                }

                R.id.navigation_pills -> {
                    val pillsFragment = PillsFragment()
                    replaceFragment(pillsFragment)
                    true
                }

                R.id.navigation_blog -> {
                    val blogFragment = BlogFragment()
                    replaceFragment(blogFragment)
                    true
                }

                else -> false
            }
        }
        mainBinding.bottomNavigation.selectedItemId = R.id.navigation_home


    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}

