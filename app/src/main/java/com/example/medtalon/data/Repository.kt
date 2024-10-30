package com.example.medtalon.data

import android.content.Context
import android.util.Log
import com.example.medtalon.domain.IRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(
    private val context: Context,
) : IRepository {
    companion object {

        @Volatile
        private var instance: IRepository? = null

        fun getInstance(context: Context):IRepository{

            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = Repository(context)
                    }
                }
            }
            return instance!!
        }
    }
  //  private val api = Api()
    private var token = ""




    override suspend fun getToken(authCode: String): String {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val savedToken = sharedPreferences.getString("token", null)
        val expirationTime = sharedPreferences.getLong("expiration_time", 0)

        val currentTime = System.currentTimeMillis() / 1000

        if (savedToken != null && expirationTime > currentTime) {
            token = savedToken
            return savedToken
        }




        val response = withContext(Dispatchers.IO) {
            //api.fetchInstagramAccessToken(authCode)
        }

      //  token = response?.accessToken ?: "null"
       // val expiresIn = api.GetApiService.refreshToken("ig_refresh_token", token)?.body()?.expires_in
            ?: 3600

        val editor = sharedPreferences.edit()
        editor.putString("token", token)
            //   editor.putLong("expiration_time", currentTime + expiresIn)
        editor.apply()
        return token
    }
    override suspend fun getUsername(): String {
        Log.d("getUsername", token)
     //   return api.getUsername(token)?.username ?: "No username"
        return ""
    }



}
