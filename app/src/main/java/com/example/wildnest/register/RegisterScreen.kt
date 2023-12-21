package com.example.wildnest.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.wildnest.R
import com.example.wildnest.databinding.ActivityRegisterScreenBinding
import com.example.wildnest.login.LoginScreen
import com.example.wildnest.model.UsersModel
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
    private lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtSignIn.setOnClickListener {
            startActivity(Intent(this@RegisterScreen, LoginScreen::class.java))
            finish()
        }

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        viewModel.registrationSuccess.observe(this) { isRegistrationSuccessful ->
            if (isRegistrationSuccessful) {
                Toast.makeText(this@RegisterScreen, "SignUp Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@RegisterScreen, LoginScreen::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@RegisterScreen, "User already exists", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.btnCreateAccount.setOnClickListener {
            val signUpUsername = binding.edtName.text.toString()
            val signUpEmail = binding.edtEmail.text.toString()
            val signUppPassword = binding.edtPassword.text.toString()

            if (signUpUsername.isNotEmpty() && signUpEmail.isNotEmpty() && signUppPassword.isNotEmpty()) {
                viewModel.signUpUser(signUpEmail, signUpUsername, signUppPassword, databaseReference)
            } else {
                Toast.makeText(this@RegisterScreen, "All Fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }
    }
//    private fun signUpUser(email: String, username: String, password: String) {
//        databaseReference.orderByChild("username").equalTo(username)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (!snapshot.exists()) {
//                        val id = databaseReference.push().key
//                        val userData = UsersModel(id, email, username, password)
//                        databaseReference.child(id!!).setValue(userData)
//                        Toast.makeText(
//                            this@RegisterScreen,
//                            "SignUp Successful",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        startActivity(Intent(this@RegisterScreen, LoginScreen::class.java))
//                        finish()
//                    } else {
//                        Toast.makeText(
//                            this@RegisterScreen,
//                            "User already exists",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Toast.makeText(
//                        this@RegisterScreen,
//                        "Database Error: $error",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            })
//    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        this@RegisterScreen.overridePendingTransition(
            R.anim.fade_in,
            R.anim.fade_out,
        )
    }
}