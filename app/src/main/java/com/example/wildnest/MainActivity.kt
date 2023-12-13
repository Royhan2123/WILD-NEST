package com.example.wildnest

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.wildnest.Camera.OutputCamera
import com.example.wildnest.Gallery.OutputGallery
import com.example.wildnest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            MainActivity.REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(MainActivity.REQUIRED_PERMISSION)
        }

        binding.btnOutputCamera.setOnClickListener(this)
        binding.btnOutputGallery.setOnClickListener(this)

        playAnimation()
    }

    private fun playAnimation() {
        binding.txtWelcome.visibility = View.VISIBLE
        binding.txtDesc.visibility = View.VISIBLE
        binding.btnOutputCamera.visibility = View.VISIBLE
        binding.btnOutputGallery.visibility = View.VISIBLE

        binding.txtWelcome.alpha = 0f
        binding.txtDesc.alpha = 0f
        binding.btnOutputCamera.alpha = 0f
        binding.btnOutputGallery.alpha = 0f

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(
            ObjectAnimator.ofFloat(binding.txtWelcome, View.ALPHA, 1.5f).setDuration(800),
            ObjectAnimator.ofFloat(binding.txtDesc, View.ALPHA, 1f).setDuration(800),
            ObjectAnimator.ofFloat(binding.btnOutputCamera, View.ALPHA, 1f).setDuration(800),
            ObjectAnimator.ofFloat(binding.btnOutputGallery, View.ALPHA, 1f).setDuration(800),
        )
        // Start animation
        animatorSet.start()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnOutputCamera -> {
            val intent = Intent(this@MainActivity, OutputCamera::class.java)
                startActivity(intent)
            }
            R.id.btnOutputGallery -> {
                val intent = Intent(this@MainActivity, OutputGallery::class.java)
                startActivity(intent)
            }
        }
    }
}