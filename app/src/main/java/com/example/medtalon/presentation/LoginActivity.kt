package com.example.medtalon.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medtalon.data.Auth
import com.example.medtalon.data.DataBase
import com.example.medtalon.domain.User
import com.example.test2.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()
    private val auth: Auth = Auth()
    private val dataBase: DataBase = DataBase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val login = binding.registerEditText.text.toString()
            val password = binding.registerPasswordEditText.text.toString()
            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
        }

        binding.btnLogin.setOnClickListener {
            val login = binding.loginEditText.text.toString()
            val password = binding.loginPasswordEditText.text.toString()
            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            homeViewModel.currentUser = login
            homeViewModel.isLogin = true
            dataBase.setUser(User("", "", login, "", "", password))
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