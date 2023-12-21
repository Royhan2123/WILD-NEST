package com.example.wildnest.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wildnest.model.UsersModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginViewModel : ViewModel() {

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    fun loginUser(email: String, password: String, databaseReference: DatabaseReference) {
        databaseReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val userData = userSnapshot.getValue(UsersModel::class.java)

                            if (userData != null && userData.password == password) {
                                _loginSuccess.value = true
                                return
                            }
                        }
                    }
                    _loginSuccess.value = false
                }

                override fun onCancelled(error: DatabaseError) {
                    _loginSuccess.value = false
                }
            })
    }
}