package com.example.medtalon.presentation


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test2.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nameTextView.text = "Информация о пользователе" // Данные профиля


        binding.backButton.setOnClickListener {
            finish() // Закрывает активити и возвращает назад
        }
    }
}
