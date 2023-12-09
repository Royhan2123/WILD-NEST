package com.example.wildnest

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.wildnest.CameraActivity.Companion.CAMERAX_RESULT
import com.example.wildnest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private var currentImageUri: Uri? = null

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
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.btnGallery.setOnClickListener { startGallery() }

        playAnimation()
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }
    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
        }
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