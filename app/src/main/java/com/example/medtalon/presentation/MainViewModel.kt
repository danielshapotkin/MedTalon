package com.example.medtalon.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medtalon.data.Repository
import com.example.medtalon.domain.IRepository

class MainViewModel(
    context: Context
) {
    companion object {
        val OAUTH_URL = "https://login.microsoftonline.com/a56d564c-1c59-4ba5-8cb6-0770c1bf8783/oauth2/v2.0/authorize? client_id=e86a9e2d-48cb-431f-8f21-b43ff05f057a &response_type=code &redirect_uri=https://aichatbotauth/callback &response_mode=query &scope=https%3A%2F%2Fgraph.microsoft.com%2Fmail.read &state=12345198e049d7d05"
    }

    private val repository: IRepository = Repository.getInstance(context)


    private val _states = MutableLiveData(States("", false))
    private val _events = MutableLiveData<Events>()

    val states: LiveData<States> get() = _states
    val events: LiveData<Events> get() = _events



    data class States(val text : String, val isShowProgress : Boolean)



    sealed class Events {
        object Nothing : Events()
        data class OpenUrl(val url : String) : Events()
        object HideButtons: Events()
        object ShowButtons: Events()
    }



    suspend fun getUsername(){
        _states.value = states.value?.copy(isShowProgress = true)
        val username = repository.getUsername()
        _states.value = states.value?.copy(text = username, false)
    }



    suspend fun getToken(authCode: String){
        _states.value = states.value?.copy(isShowProgress = true)
        val token =  repository.getToken(authCode)
        _states.value = states.value?.copy(text = token, isShowProgress = false)
    }

    fun getAuthCode() {
        _events.value  = Events.OpenUrl(OAUTH_URL)
    }
    fun hideButtons(){
        _events.value = Events.HideButtons
    }
    fun showButtons(){
        _events.value  = Events.ShowButtons
    }
}
