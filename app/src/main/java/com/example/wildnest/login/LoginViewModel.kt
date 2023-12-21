package com.example.wildnest.login

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _registeredUsername = MutableLiveData<String>()
    val registeredUsername: LiveData<String> = _registeredUsername

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val username = auth.currentUser?.displayName ?: ""
                    saveUsernameToSharedPreferences(username)
                    _loginSuccess.value = true
                    _registeredUsername.value = username
                } else {
                    _loginSuccess.value = false
                }
            }
    }

    fun getDisplayName(): String {
        return auth.currentUser?.displayName ?: ""
    }

    private fun saveUsernameToSharedPreferences(username: String) {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.apply()
    }
}
