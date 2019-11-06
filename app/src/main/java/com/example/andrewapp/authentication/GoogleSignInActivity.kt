package com.example.andrewapp.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.andrewapp.BaseApplication
import com.example.andrewapp.Constants.GOOGLE_ID_PROVIDER
import com.example.andrewapp.R
import com.example.andrewapp.authentication.AuthenticationErrors.*
import com.example.andrewapp.data.User
import com.example.andrewapp.home.MainActivity
import com.example.andrewapp.viewmodel.viewmodelimpl.AuthViewModelImpl
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_google_signin.*
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class GoogleSignInActivity : AppCompatActivity() {
    companion object {
        const val RC_SIGN_IN = 3310
        val TAG = this::class.java.name
    }

    private var mDisposables = CompositeDisposable()

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mFirebaseAuth: FirebaseAuth

    @Inject
    lateinit var appContext: Context

    @Inject
    lateinit var mAuthViewModel: AuthViewModelImpl


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_signin)
        (application as BaseApplication).appComponent.inject(this)

        setUpGoogleSignInClient()

        setupFirebaseAuthentication()

        setupUI()
    }

    private fun setupFirebaseAuthentication() {
        mFirebaseAuth = FirebaseAuth.getInstance()
    }

    private fun setUpGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()
        //todo try changing up the context to use application context instead.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun setupUI() {
        sign_in_button.setOnClickListener { attemptGoogleSignIn() }
    }

    override fun onStart() {
        super.onStart()
        if (mAuthViewModel.isLoggedIn()) {
            switchToHomeScreen()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_SIGN_IN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleGoogleSignInResult(task)
            }
        }
    }

    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)

            val oauthToken =
                account?.idToken ?: throw ApiException(Status(400, "Google error: No account info returned"))
            Timber.d("###### Google id_token: ${account.idToken}")

            //TODO: figure our blocker on rest api auth with google
            // (error message: INVALID_IDP_RESPONSE : Unable to verify the ID Token signature.)
            // Note: error is not encountered when using curl in a terminal with same credentials :-(
            //firebaseAuthWithGoogle(oauthToken)

            // todo: remove this method once rest api google authentication is working
            authenticationWithFirebaseSDK(account)
        } catch (exception: ApiException) {
            displaySnackBar(exception.message ?: "Google sign in failed...")
        }
    }

    private fun firebaseAuthWithGoogle(oauthToken: String) {
        val onNext = Consumer<User> {
            mAuthViewModel.saveUserTokensToSharedPreferences(it)

            //TODO complete implementation for save/updating a user's info
            mAuthViewModel.saveOrUpdateUser(it)

            switchToHomeScreen()
        }
        val onError = Consumer<Throwable> {
            when (it) {
                is HttpException -> handleDisplayHttpErrors(it)
            }
        }
        val onComplete = Action {
            //stop loading ui
        }

        val disposable = mAuthViewModel.authenticateOauthUser(oauthToken, GOOGLE_ID_PROVIDER)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext, onError, onComplete)

        mDisposables.add(disposable)
    }

    private fun handleDisplayHttpErrors(exception: HttpException) {
        val errorAsJson = exception.response()?.errorBody()?.string()
        val authError = Gson().fromJson(errorAsJson, FirebaseAuthErrorWrapper::class.java)
        AuthenticationErrors()

        Timber.d("###### Google auth error: $authError")

        when (authError.toString()) {
            OPERATION_NOT_ALLOWED -> displaySnackBar(errorHashMap[OPERATION_NOT_ALLOWED]!!)
            INVALID_IDP_RESPONSE -> displaySnackBar(errorHashMap[INVALID_IDP_RESPONSE]!!)
            INVALID_ID_TOKEN -> displaySnackBar(errorHashMap[INVALID_ID_TOKEN]!!)
            TOO_MANY_ATTEMPTS_TRY_LATER -> displaySnackBar(errorHashMap[TOO_MANY_ATTEMPTS_TRY_LATER]!!)
            USER_DISABLED -> displaySnackBar(errorHashMap[USER_DISABLED]!!)
            else -> displaySnackBar(authError.toString())
        }
    }


    private fun attemptGoogleSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    //TODO: move all firebase auth logic into the AuthViewModel to follow the MVVM architecture
    private fun authenticationWithFirebaseSDK(account: GoogleSignInAccount?) {
        if (account == null) {
            displaySnackBar("Authentication Failed.")
            return
        }

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mFirebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    updateUI(mFirebaseAuth.currentUser)

                } else {
                    Timber.e("###### Auth Failed: ${task.exception}")

                    displaySnackBar("Authentication Failed.")
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            extractFirebaseIdToken(user)
        }
    }

    private fun extractFirebaseIdToken(firebaseUser: FirebaseUser) {
        firebaseUser.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result != null) {
                        Timber.d("###### firebaseIdToken: ${task.result?.token}")
                        val idToken = task.result?.token ?: ""
                        mAuthViewModel.mAuthSharedPreferences.setLoginState(true, idToken)

                        switchToHomeScreen()
                    }
                } else {
                    displaySnackBar("Failed to retrieve firebase identifying token.")
                }
            }
    }

    private fun displaySnackBar(message: String) {
        Snackbar.make(auth_layout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun switchToHomeScreen() {
        val homeActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(homeActivityIntent)
    }
}
