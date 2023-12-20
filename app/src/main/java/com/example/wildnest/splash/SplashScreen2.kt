package com.example.wildnest.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.wildnest.login.LoginScreen
import com.example.wildnest.R
import com.example.wildnest.databinding.ActivitySplashScreen2Binding


@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashScreen2 : AppCompatActivity(){
    private lateinit var binding: ActivitySplashScreen2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            // Jika belum login, tampilkan SplashScreen2
            binding = ActivitySplashScreen2Binding.inflate(layoutInflater)
            setContentView(binding.root)

            playAnimation()
            binding.btnStart.setOnClickListener{
                val intent = Intent(this, LoginScreen::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)

                this@SplashScreen2.overridePendingTransition(
                    R.anim.fade_in,
                    R.anim.fade_out,
                )
            }
}


    private fun playAnimation() {
        binding.backgroundLayout.visibility = View.VISIBLE
        binding.imgView.visibility = View.VISIBLE
        binding.txtView.visibility = View.VISIBLE
        binding.txtDesc.visibility = View.VISIBLE
        binding.btnStart.visibility = View.VISIBLE

        binding.backgroundLayout.alpha = 0f
        binding.imgView.alpha = 0f
        binding.txtView.alpha = 0f
        binding.txtDesc.alpha = 0f
        binding.btnStart.alpha = 0f

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(
            ObjectAnimator.ofFloat(binding.backgroundLayout, View.ALPHA, 1.5f).setDuration(800),
            ObjectAnimator.ofFloat(binding.imgView, View.ALPHA, 1.5f).setDuration(800),
            ObjectAnimator.ofFloat(binding.txtView, View.ALPHA, 1f).setDuration(800),
            ObjectAnimator.ofFloat(binding.txtDesc, View.ALPHA, 1f).setDuration(800),
            ObjectAnimator.ofFloat(binding.btnStart, View.ALPHA, 1f).setDuration(800),
        )
        // Start animation
        animatorSet.start()
    }
}