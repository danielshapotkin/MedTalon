package com.example.medtalon.presentation

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medtalon.data.App
import com.example.medtalon.data.Auth
import com.example.medtalon.data.DataBase

class HomeViewModel private constructor() {

    var isLogin = false
    private val _events = MutableLiveData<Events>()
    val events: LiveData<Events> get() = _events
    private val auth: Auth = Auth()
    private val dataBase: DataBase = DataBase.getInstance()


    init {
        val sharedPrefs = App.context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        isLogin = sharedPrefs.getBoolean("is_login", false)
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

    sealed class Events {
        object Login : Events()
        data class Regions(val view: View) : Events()
        object Talon : Events()
        object Doctor : Events()
        object MedicalInstitutions : Events()
        object PayServices : Events()
        object Search : Events()
        object Profile : Events()
    }

    private fun saveLoginState(isLoggedIn: Boolean) {
        val sharedPrefs = App.context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean("is_login", isLoggedIn).apply()
    }

    fun showProfile() {
        if (!isLogin) {
            _events.value = Events.Login
        } else {
            _events.value = Events.Profile
        }
        _events.value = null
    }

    fun register(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.registerUser(email, password) { isSuccess, message ->
            message?.let { Log.d("MyLogs", it) }
        }
    }

    fun login(email: String, password: String) {
        auth.loginUser(email, password) { isSuccess, message ->
            message?.let { Log.d("LoginLogs", it) }
            if (isSuccess) {
                isLogin = true
                saveLoginState(true)

                dataBase.linkUserToClinic(userId = auth.getCurrentUserId(), clinicId = "1", onSuccess = {
                    println("Пользователь привязан к поликлинике")
                },
                    onError = { exception->
                        println("Произошла ошибка ${exception.message}")
                    })


            }
        }

    }

    fun logout() {
        isLogin = false
        saveLoginState(false)
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
