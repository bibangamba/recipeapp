package com.example.andrewapp.authentication

import android.content.Context
import javax.inject.Inject

class AuthSharedPreferences @Inject constructor(appContext: Context) {

    private val mAuthSharedPreferences = appContext.getSharedPreferences(
        AUTH_SHARED_PREFS,
        Context.MODE_PRIVATE
    )

    companion object {
        const val AUTH_SHARED_PREFS = "AUTH_SHARED_PREFS"
        const val IS_LOGGED_IN = "IS_LOGGED_IN"
        const val ID_TOKEN = "ID_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val DEFAULT_LOGIN_STATE = false
        const val DEFAULT_ID_TOKEN = ""
        const val DEFAULT_REFRESH_TOKEN = ""
    }

    fun setLoginState(isLoggedIn: Boolean, idToken: String, refreshToken: String) =
        mAuthSharedPreferences.edit()
            .putBoolean(IS_LOGGED_IN, isLoggedIn)
            .putString(ID_TOKEN, idToken)
            .putString(REFRESH_TOKEN, refreshToken)
            .apply()

    fun setLoginState(isLoggedIn: Boolean, idToken: String) =
        mAuthSharedPreferences.edit()
            .putBoolean(IS_LOGGED_IN, isLoggedIn)
            .putString(ID_TOKEN, idToken)
            .apply()

    fun isLoggedIn() = mAuthSharedPreferences.getBoolean(IS_LOGGED_IN, DEFAULT_LOGIN_STATE)

    fun getIdToken() = mAuthSharedPreferences.getString(ID_TOKEN, DEFAULT_ID_TOKEN)!!

    fun getRefreshToken() = mAuthSharedPreferences.getString(REFRESH_TOKEN, DEFAULT_REFRESH_TOKEN)

}
