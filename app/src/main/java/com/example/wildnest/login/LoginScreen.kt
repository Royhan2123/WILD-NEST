package com.example.wildnest.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.wildnest.MainActivity
import com.example.wildnest.R
import com.example.wildnest.register.RegisterScreen
import com.example.wildnest.databinding.ActivityLoginScreenBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

@Suppress("DEPRECATION")
class LoginScreen : AppCompatActivity() {
    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")
        binding.txtSignUp.setOnClickListener {
            startActivity(Intent(this@LoginScreen, RegisterScreen::class.java))
        }

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            val loginEmail = binding.edtEmail.text.toString()
            val loginPassword = binding.edtPassword.text.toString()

            if (loginEmail.isNotEmpty() && loginPassword.isNotEmpty()) {
                viewModel.loginUser(loginEmail, loginPassword)
            } else {
                Toast.makeText(this@LoginScreen, "All Fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loginSuccess.observe(this) { isLoginSuccessful ->
            if (isLoginSuccessful) {
                val intent = Intent(this@LoginScreen, MainActivity::class.java)
                startActivity(intent)

                this@LoginScreen.overridePendingTransition(
                    R.anim.fade_in,
                    R.anim.fade_out,
                )
                finish()
            } else {
                Toast.makeText(this@LoginScreen, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()

        this@LoginScreen.overridePendingTransition(
            R.anim.fade_in,
            R.anim.fade_out,
        )
    }
}
