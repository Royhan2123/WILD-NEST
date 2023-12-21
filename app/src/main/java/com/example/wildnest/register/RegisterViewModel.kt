package com.example.wildnest.register

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wildnest.model.UsersModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean> = _registrationSuccess

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

    fun signUpUser(email: String, username: String, password: String) {
        databaseReference.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val firebaseUser = auth.currentUser
                                    firebaseUser?.let {
                                        val userId = it.uid
                                        // Set display name
                                        it.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(username).build())
                                        val userData = UsersModel(userId, username, email, password)
                                        databaseReference.child(userId).setValue(userData)
                                    }
                                    saveUsernameToSharedPreferences(username)
                                    _registrationSuccess.value = true
                                } else {
                                    _registrationSuccess.value = false
                                }
                            }
                    } else {
                        _registrationSuccess.value = false
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    _registrationSuccess.value = false
                }
            })
    }

    private fun saveUsernameToSharedPreferences(username: String) {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.apply()
    }
}

//    fun signUpUser(email: String, username: String, password: String, databaseReference: DatabaseReference) {
//        databaseReference.orderByChild("username").equalTo(username)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (!snapshot.exists()) {
//                        val id = databaseReference.push().key
//                        val userData = UsersModel(id, username, email, password)
//                        databaseReference.child(id!!).setValue(userData)
//                        _registrationSuccess.value = true
//                    } else {
//                        _registrationSuccess.value = false
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    _registrationSuccess.value = false
//                }
//            })
//    }