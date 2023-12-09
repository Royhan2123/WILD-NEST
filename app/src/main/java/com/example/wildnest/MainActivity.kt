package com.example.wildnest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.wildnest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.container.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
           R.id.container -> {
               val intent = Intent(this,CameraActivity::class.java)
               startActivity(intent)
           }
        }
    }
}