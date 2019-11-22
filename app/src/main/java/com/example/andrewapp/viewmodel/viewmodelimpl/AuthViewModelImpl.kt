package com.example.andrewapp.viewmodel.viewmodelimpl

import androidx.lifecycle.ViewModel
import com.example.andrewapp.authentication.AuthSharedPreferences
import com.example.andrewapp.db.User
import com.example.andrewapp.service.IdentityProviderAuthService
import com.example.andrewapp.viewmodel.AuthViewModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthViewModelImpl @Inject constructor(private val identityProviderAuthService: IdentityProviderAuthService) :
    AuthViewModel,
    ViewModel() {

    @Inject
    lateinit var mAuthSharedPreferences: AuthSharedPreferences

    override fun saveOrUpdateUser(user: User): Completable {
        TODO("not implemented")
    }

    override fun saveUserTokensToSharedPreferences(user: User) {
        mAuthSharedPreferences.setLoginState(true, user.idToken, user.refreshToken)
    }

    override fun exchangeRefreshTokenForIdToken(refreshToken: String): Flowable<User> {
        TODO("not implemented")
    }

    override fun authenticateOauthUser(oauthIdToken: String, idProvider: String): Flowable<User> {
        val postBody = "id_token={$oauthIdToken}&providerId=$idProvider"
        return identityProviderAuthService.idpSignIn(postBody = postBody, requestUri = "http://localhost")
            .subscribeOn(Schedulers.io())
    }

    override fun isLoggedIn(): Boolean {
        return mAuthSharedPreferences.isLoggedIn()
    }

    override fun getCachedIdToken(): String {
        return mAuthSharedPreferences.getIdToken()
    }

    override fun getCachedRefreshToken(): String {
        TODO("not implemented")
    }
}
