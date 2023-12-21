package com.example.wildnest.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.wildnest.MainActivity
import com.example.wildnest.R
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val currentUser = FirebaseAuth.getInstance().currentUser

        Handler().postDelayed({
            if (currentUser != null) {
                // Jika pengguna sudah login, arahkan langsung ke MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                // Jika pengguna belum login, arahkan ke SplashScreen2
                val intent = Intent(this, SplashScreen2::class.java)
                startActivity(intent)
            }

            this@SplashScreen.overridePendingTransition(
                R.anim.fade_in,
                R.anim.fade_out,
            )
            finish()
        }, 3000)
    }
}
