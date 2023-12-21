package com.example.wildnest.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.wildnest.R
import com.example.wildnest.databinding.ActivityRegisterScreenBinding
import com.example.wildnest.login.LoginScreen
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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
            this@RegisterScreen.overridePendingTransition(
                R.anim.fade_in,
                R.anim.fade_out,
            )
            finish()
        }

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        binding.btnCreateAccount.setOnClickListener {
            val signUpUsername = binding.edtName.text.toString()
            val signUpEmail = binding.edtEmail.text.toString()
            val signUpPassword = binding.edtPassword.text.toString()

            if (signUpUsername.isNotEmpty() && signUpEmail.isNotEmpty() && signUpPassword.isNotEmpty()) {
                // Show the ProgressBar when the registration process starts
                binding.progressBar.visibility = View.VISIBLE
                viewModel.signUpUser(signUpEmail, signUpUsername, signUpPassword)
            } else {
                Toast.makeText(this@RegisterScreen, "All Fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.registrationSuccess.observe(this) { isRegistrationSuccessful ->
            // Hide the ProgressBar when the registration process is complete
            binding.progressBar.visibility = View.INVISIBLE

            if (isRegistrationSuccessful) {
                Toast.makeText(this@RegisterScreen, "SignUp Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@RegisterScreen, LoginScreen::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@RegisterScreen, "User already exists", Toast.LENGTH_SHORT).show()
            }
        }
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