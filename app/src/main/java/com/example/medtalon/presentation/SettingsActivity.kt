package com.example.medtalon.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.test2.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        val notificationsButton = findViewById<Button>(R.id.notifications_button)
        val languageButton = findViewById<Button>(R.id.language_button)
        val themeButton = findViewById<Button>(R.id.theme_button)
        val privacyPolicyButton = findViewById<Button>(R.id.privacyPolicyButton)
        val logoutButton = findViewById<Button>(R.id.logout_button)

        notificationsButton.setOnClickListener { openNotificationsSettings() }
        languageButton.setOnClickListener { openLanguageSettings() }
        themeButton.setOnClickListener { openThemeSettings() }
        privacyPolicyButton.setOnClickListener { openPrivacyPolicy() }
        logoutButton.setOnClickListener { logoutUser() }
    }

    private fun openNotificationsSettings() {
        val notificationOptions = arrayOf("Включить уведомления", "Выключить уведомления")
        val currentSetting = if (sharedPreferences.getBoolean("notificationsEnabled", true)) 0 else 1

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Настройки уведомлений")
        builder.setSingleChoiceItems(notificationOptions, currentSetting) { dialog, which ->
            val notificationsEnabled = which == 0
            sharedPreferences.edit().putBoolean("notificationsEnabled", notificationsEnabled).apply()
            val message = if (notificationsEnabled) "Уведомления включены" else "Уведомления выключены"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        builder.show()
    }

    private fun openLanguageSettings() {
        val languages = arrayOf("Русский", "English", "Español")
        val currentLanguage = sharedPreferences.getString("language", "Русский")
        val selectedLanguageIndex = languages.indexOf(currentLanguage)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Выберите язык")
        builder.setSingleChoiceItems(languages, selectedLanguageIndex) { dialog, which ->
            val selectedLanguage = languages[which]
            sharedPreferences.edit().putString("language", selectedLanguage).apply()
            Toast.makeText(this, "Выбран язык: $selectedLanguage", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        builder.show()
    }

    private fun openThemeSettings() {
        val themes = arrayOf("Светлая", "Темная")
        val isDarkTheme = sharedPreferences.getBoolean("isDarkTheme", false)
        val selectedThemeIndex = if (isDarkTheme) 1 else 0

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Выберите тему")
        builder.setSingleChoiceItems(themes, selectedThemeIndex) { dialog, which ->
            val isDarkMode = which == 1
            sharedPreferences.edit().putBoolean("isDarkTheme", isDarkMode).apply()
            setTheme(isDarkMode)
            dialog.dismiss()
        }
        builder.show()
    }

    private fun setTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Toast.makeText(this, "Темная тема включена", Toast.LENGTH_SHORT).show()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Toast.makeText(this, "Светлая тема включена", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openPrivacyPolicy() {
        val url = "https://leki.talon.by/personal-data"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun logoutUser() {
        sharedPreferences.edit().clear().apply()
        Toast.makeText(this, "Выход из профиля", Toast.LENGTH_SHORT).show()
        finish()
    }
}
