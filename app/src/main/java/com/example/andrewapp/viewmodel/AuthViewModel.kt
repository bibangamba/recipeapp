package com.example.andrewapp.viewmodel

import com.example.andrewapp.data.User
import io.reactivex.Completable
import io.reactivex.Flowable

interface AuthViewModel {
    fun exchangeRefreshTokenForIdToken(refreshToken: String): Flowable<User>

    fun isLoggedIn(): Boolean

    fun getCachedIdToken(): String

    fun getCachedRefreshToken(): String

    fun saveUserTokensToSharedPreferences(user: User)

    fun saveOrUpdateUser(user: User): Completable

//     fun updateUser(user: User): Completable

    fun authenticateOauthUser(oauthIdToken: String, idProvider: String): Flowable<User>
}

//abstract class AuthViewModel @Inject constructor(val googleAuthService: IdentityProviderAuthService) : ViewModel() {
//    abstract fun exchangeRefreshTokenForIdToken(refreshToken: String): Flowable<User>
//
//    abstract fun isLoggedIn(): Boolean
//
//    abstract fun getCachedIdToken(): String
//
//    abstract fun getCachedRefreshToken(): String
//
//    abstract fun saveUserTokensToSharedPreferences(user: User)
//
//    abstract fun saveOrUpdateUser(user: User): Completable
//
////    abstract fun updateUser(user: User): Completable
//
//    abstract fun authenticateOauthUser(oauthIdToken: String, idProvider: String): Flowable<User>
//}
