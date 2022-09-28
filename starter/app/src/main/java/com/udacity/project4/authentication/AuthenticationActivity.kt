package com.udacity.project4.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.locationreminders.RemindersActivity
import kotlinx.android.synthetic.main.activity_authentication.*


/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
const val SIGN_IN_CODE = 2020

class AuthenticationActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var authenticationLayout : AuthMethodPickerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
//         TODO: Implement the create account and sign in using FirebaseUI, use sign in using email and sign in using Google
        login_btn.setOnClickListener {
            launchSignInFlow()
        }
//          TODO: If the user was authenticated, send him to RemindersActivity
        viewModel.authenticationState.observe(this, Observer {
            if (it == LoginViewModel.AuthenticationState.AUTHENTICATED) {
                startActivity(Intent(applicationContext, RemindersActivity::class.java))
                finish()
            }

        })

//          TODO: a bonus is to customize the sign in flow to look nice using :
        //https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md#custom-layout
         authenticationLayout = AuthMethodPickerLayout.Builder(R.layout.authentication_custom_layout)
            .setGoogleButtonId(R.id.google_sign_in_btn)
            .setEmailButtonId(R.id.email_sign_in_btn)
            .build()

    }


    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setAuthMethodPickerLayout(authenticationLayout)
                .build(),
            SIGN_IN_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in user.
                Log.i(
                    "Ahmed", "Successfully signed in user " + "${FirebaseAuth.getInstance().currentUser?.displayName}!"
                )

            } else {

                Log.i("Ahmed", "Sign in unsuccessful ${response?.error?.errorCode}")
                Toast.makeText(applicationContext, R.string.sign_in_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

}

