package com.example.wildnest.register

import androidx.lifecycle.ViewModel
import com.example.wildnest.model.UsersModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterViewModel : ViewModel() {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("users")

    fun signUpUser(email: String, username: String, password: String, onResult: (String) -> Unit) {
        databaseReference.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        val id = databaseReference.push().key
                        val userData = UsersModel(id, email, username, password)
                        databaseReference.child(id!!).setValue(userData)
                        onResult("SignUp Successful")
                    } else {
                        onResult("User already exists")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onResult("Database Error: $error")
                }
            })
    }
}