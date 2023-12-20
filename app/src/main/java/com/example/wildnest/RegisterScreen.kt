package com.example.wildnest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.wildnest.databinding.ActivityRegisterScreenBinding

@Suppress("DEPRECATION")
class RegisterScreen : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding:ActivityRegisterScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtSignIn.setOnClickListener(this)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        this@RegisterScreen.overridePendingTransition(
            R.anim.fade_in,
            R.anim.fade_out,
        )
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.txtSignIn -> {
                val intent = Intent(this@RegisterScreen,LoginScreen::class.java)
                startActivity(intent)

                this@RegisterScreen.overridePendingTransition(
                    R.anim.fade_in,
                    R.anim.fade_out,
                )
            }
        }
    }
}