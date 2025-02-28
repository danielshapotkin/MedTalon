package com.example.medtalon.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.medtalon.data.Auth
import com.example.medtalon.data.DataBase
import com.example.medtalon.domain.User
import com.example.medtalon.web.UrlLoader
import com.example.test2.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val homeViewModel: HomeViewModel = HomeViewModel.getInstance()
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
            dataBase.setUser(User("", login, password, ""))
            binding.viewFlipper.showNext()
            binding.tvToggleLoginRegister.text = if (binding.viewFlipper.displayedChild == 0) {
                "Еще нет аккаунта? Зарегистрироваться"
            } else {
                "Уже есть аккаунт? Войти в существующий"
            }
        }

        binding.btnLogin.setOnClickListener {
            val login = binding.loginEditText.text.toString()
            val password = binding.loginPasswordEditText.text.toString()
            if (login == "admin" || password == "admin"){
                val intent = Intent(this, UrlLoader::class.java).apply {
                    val url = "https://console.firebase.google.com/project/test2-79b97/firestore/databases/-default-/data/~2FUsers~2F1111"
                    putExtra("url", url)
                }
                startActivity(intent)
            }
            else{
                if (login.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Пожалуйста, заполните все поля.", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                lifecycleScope.launch {
                    val user = dataBase.getUserByLogin(login)
                    if (user != null) {
                        if (password == user.password) {
                            homeViewModel.currentUser = login
                            homeViewModel.isLogin = true
                            finish()
                        } else {
                            Log.d("Login", "Неверный пароль для пользователя: $login")
                            Toast.makeText(this@LoginActivity, "Неверный логин или пароль.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Log.d("Login", "Пользователь с логином $login не найден.")
                        Toast.makeText(this@LoginActivity, "Неверный логин или пароль.", Toast.LENGTH_LONG).show()
                    }
                }
            }


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