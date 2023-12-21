package com.example.wildnest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.wildnest.databinding.ActivityAccountScreenBinding
import com.example.wildnest.login.LoginScreen
import com.example.wildnest.login.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class AccountScreen : AppCompatActivity() {
    private lateinit var binding: ActivityAccountScreenBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        // Mengatur teks TextView sesuai dengan data pengguna yang sudah login
        binding.txtName.text = viewModel.getDisplayName()
        binding.txtEmail.text = viewModel.getEmail()

        binding.btnLogout.setOnClickListener {
            // Melakukan logout menggunakan Firebase Authentication
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, LoginScreen::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            this@AccountScreen.overridePendingTransition(
                R.anim.fade_in,
                R.anim.fade_out,
            )
            finish()
        }
    }
}