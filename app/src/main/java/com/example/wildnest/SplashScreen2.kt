package com.example.wildnest

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.wildnest.databinding.ActivitySplashScreen2Binding

@SuppressLint("CustomSplashScreen")
class SplashScreen2 : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySplashScreen2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreen2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        binding.btnStart.setOnClickListener(this)
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

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnStart -> {
                val intent = Intent(this,LoginScreen::class.java)
                startActivity(intent)
            }
        }
    }

}