package com.example.medtalon.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.test2.R
import com.example.test2.databinding.ActivityLoginBinding
import com.example.test2.databinding.ActivityMainBinding
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

    binding.btnRegister.setOnClickListener{
        val login = binding.registerEditText.text.toString()
        val password = binding.registerPasswordEditText.text.toString()
        homeViewModel.register(login, password){isSuccesfull, message->
            if (isSuccesfull){binding.viewFlipper.showNext()}
        }
    }

        binding.btnLogin.setOnClickListener {
            val login = binding.loginEditText.text.toString()
            val password = binding.loginPasswordEditText.text.toString()
            homeViewModel.login(login, password)
            finish()
        }

        binding.privacyPolicyText.setOnClickListener {
            val url = "https://leki.talon.by/personal-data"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.tvToggleLoginRegister.setOnClickListener {
            binding.viewFlipper.showNext()
            binding.tvToggleLoginRegister.text = if (binding.viewFlipper.displayedChild == 0) {
                "Еще нет аккаунта? Зарегистрироваться"
            } else {
                "Уже есть аккаунт? Войти в существующий"
            }
        }
    }
}