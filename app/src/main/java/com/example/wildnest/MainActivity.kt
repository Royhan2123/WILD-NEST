package com.example.wildnest

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.wildnest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
    }

    private fun playAnimation() {
        binding.txtWelcome.visibility = View.VISIBLE
        binding.txtDesc.visibility = View.VISIBLE
        binding.btnCamera.visibility = View.VISIBLE
        binding.btnGallery.visibility = View.VISIBLE

        binding.txtWelcome.alpha = 0f
        binding.txtDesc.alpha = 0f
        binding.btnCamera.alpha = 0f
        binding.btnGallery.alpha = 0f

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(
            ObjectAnimator.ofFloat(binding.txtWelcome, View.ALPHA, 1.5f).setDuration(800),
            ObjectAnimator.ofFloat(binding.txtDesc, View.ALPHA, 1f).setDuration(800),
            ObjectAnimator.ofFloat(binding.btnCamera, View.ALPHA, 1f).setDuration(800),
            ObjectAnimator.ofFloat(binding.btnGallery, View.ALPHA, 1f).setDuration(800),
        )
        // Start animation
        animatorSet.start()
    }
}