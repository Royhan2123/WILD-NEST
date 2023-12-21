package com.example.wildnest.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wildnest.model.UsersModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterViewModel : ViewModel() {

    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean> = _registrationSuccess

    fun signUpUser(email: String, username: String, password: String, databaseReference: DatabaseReference) {
        databaseReference.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        val id = databaseReference.push().key
                        val userData = UsersModel(id, username, email, password)
                        databaseReference.child(id!!).setValue(userData)
                        _registrationSuccess.value = true
                    } else {
                        _registrationSuccess.value = false
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    _registrationSuccess.value = false
                }
            })
    }
}