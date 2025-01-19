package com.example.medtalon.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medtalon.presentation.HomeViewModel.Events
import com.example.test2.R
import com.example.test2.databinding.ActivityMainBinding
import com.example.test2.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var bottomNavigation: BottomNavigationView
    private val fs = Firebase.firestore
    private val storage = Firebase.storage.reference
    private val _events = MutableLiveData<Events>()
    val events: LiveData<Events> get() = _events


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        homeBinding = FragmentHomeBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)




        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
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
        bottomNavigation.selectedItemId = R.id.navigation_home


    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}

