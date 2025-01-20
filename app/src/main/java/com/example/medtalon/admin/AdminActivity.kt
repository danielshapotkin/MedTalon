package com.example.medtalon.admin

import UsersAdapter
import android.content.Intent
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medtalon.data.DataBase
import com.example.medtalon.presentation.HomeViewModel
import com.example.medtalon.presentation.ProfileActivity
import com.example.medtalon.presentation.SettingsActivity
import com.example.medtalon.web.UrlLoader
import com.example.test2.R
import com.example.test2.databinding.ActivityAdminBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val url = "https://console.firebase.google.com/project/test2-79b97/firestore/databases/-default-/data/~2FUsers~2F1111"
        val intent = Intent(this, UrlLoader::class.java).apply {
            putExtra("url", url)
        }
        startActivity(intent)
    }
}