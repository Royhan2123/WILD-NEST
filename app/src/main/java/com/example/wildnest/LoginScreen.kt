package com.example.wildnest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.wildnest.databinding.ActivityLoginScreenBinding

@Suppress("DEPRECATION")
class LoginScreen : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding:ActivityLoginScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtSignUp.setOnClickListener(this)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()

        this@LoginScreen.overridePendingTransition(
            R.anim.fade_in,
            R.anim.fade_out,
        )
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.txtSignUp -> {
                val intent = Intent(this@LoginScreen,RegisterScreen::class.java)
                startActivity(intent)

                this@LoginScreen.overridePendingTransition(
                    R.anim.fade_in,
                    R.anim.fade_out,
                )
            }
        }
    }
}