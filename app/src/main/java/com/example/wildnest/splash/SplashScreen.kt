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
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        firebaseAuth = FirebaseAuth.getInstance()
        if (isUserLoggedIn()) {
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                this@SplashScreen.overridePendingTransition(
                    R.anim.fade_in,
                    R.anim.fade_out,
                )
                finish()
            }, 3000)
        } else {
            Handler().postDelayed({
                val intent = Intent(this, SplashScreen2::class.java)
                startActivity(intent)
                this@SplashScreen.overridePendingTransition(
                    R.anim.fade_in,
                    R.anim.fade_out,
                )
                finish()
            }, 3000)
        }
    }
    private fun isUserLoggedIn():Boolean{
        return firebaseAuth.currentUser != null
    }
}