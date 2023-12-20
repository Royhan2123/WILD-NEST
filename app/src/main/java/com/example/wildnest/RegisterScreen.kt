package com.example.wildnest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import android.view.View
import android.widget.Toast
import com.example.wildnest.databinding.ActivityRegisterScreenBinding
import com.example.wildnest.model.UsersModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Suppress("DEPRECATION")
class RegisterScreen : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterScreenBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtSignIn.setOnClickListener {
            startActivity(Intent(this@RegisterScreen,LoginScreen::class.java))
            finish()
        }

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")
        binding.btnCreateAccount.setOnClickListener {
            val signUpUsername = binding.edtName.text.toString()
            val signUpEmail = binding.edtEmail.text.toString()
            val signUppPassword = binding.edtPassword.text.toString()

            if (signUpUsername.isNotEmpty() && signUpEmail.isNotEmpty() && signUppPassword.isNotEmpty()){
                signUpUser(signUpUsername,signUpEmail,signUppPassword)
            }else {
                Toast.makeText(this@RegisterScreen, "All Fields are mandantory", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signUpUser(email:String,username: String, password: String) {
        databaseReference.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        val id = databaseReference.push().key
                        val userData = UsersModel(id, email,username, password)
                        databaseReference.child(id!!).setValue(userData)
                        Toast.makeText(this@RegisterScreen, "SignUp Succesful", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(this@RegisterScreen, LoginScreen::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@RegisterScreen,
                            "User already exist",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@RegisterScreen, "Database Error: $error", Toast.LENGTH_SHORT).show()
                }
            })
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        this@RegisterScreen.overridePendingTransition(
            R.anim.fade_in,
            R.anim.fade_out,
        )
    }
}