package com.example.medtalon.presentation

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medtalon.App

class HomeViewModel private constructor() {

    var isLogin = false
        private set  // Закрываем возможность изменения извне
    private val _states = MutableLiveData(States("", false))
    private val _events = MutableLiveData<Events>()

    val states: LiveData<States> get() = _states
    val events: LiveData<Events> get() = _events

    init {
        val sharedPrefs = App.context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        isLogin = sharedPrefs.getBoolean("is_login", false)
    }

    data class States(val text: String, val isShowProgress: Boolean)

    sealed class Events {
        object Login : Events()
        data class Regions(val view: View) : Events()
        object Talon: Events()
        object Doctor: Events()
        object MedicalInstitutions: Events()
        object PayServices: Events()
        object Search: Events()
        object Profile: Events()
    }

    companion object {
        @Volatile
        private var INSTANCE: HomeViewModel? = null

        fun getInstance(): HomeViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: HomeViewModel().also { INSTANCE = it }
            }
        }
    }

    fun showProfile() {
        if (!isLogin) {
            _events.value = Events.Login
        } else {
            Log.d("EventsProfile", "EventsProfile")
            _events.value = Events.Profile
        }
        _events.value = null
    }

    fun login() {
        isLogin = true
        saveLoginState(true)
    }

    // Метод для выхода из системы и обновления состояния
    fun logout() {
        isLogin = false
        saveLoginState(false)
    }

    private fun saveLoginState(isLoggedIn: Boolean) {
        val sharedPrefs = App.context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean("is_login", isLoggedIn).apply()
    }

    fun showRegions(view: View) {
        _events.value = Events.Regions(view)
    }

    fun getTalon() {
        _events.value = Events.Talon
    }

    fun callDoctor() {
        _events.value = Events.Doctor
    }

    fun search() {
        _events.value = Events.Search
    }

    fun showMedicalInstitutions() {
        _events.value = Events.MedicalInstitutions
    }

    fun showPaidServices() {
        _events.value = Events.PayServices
    }
}
