package com.example.wildnest.login

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _registeredUsername = MutableLiveData<String>()
    private val _registeredEmail = MutableLiveData<String>()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val username = user?.displayName ?: ""
                    val userEmail = user?.email ?: ""

                    saveUsernameToSharedPreferences(username)
                    _loginSuccess.value = true
                    _registeredUsername.value = username
                    _registeredEmail.value = userEmail
                } else {
                    _loginSuccess.value = false
                }
            }
    }

    fun getDisplayName(): String {
        return auth.currentUser?.displayName ?: ""
    }

    fun getEmail(): String {
        return auth.currentUser?.email ?: ""
    }

    private fun saveUsernameToSharedPreferences(username: String) {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.apply()
    }
}
