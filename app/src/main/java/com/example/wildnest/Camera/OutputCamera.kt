package com.example.wildnest.Camera

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.example.wildnest.Camera.CameraScreen.Companion.CAMERAX_RESULT
import com.example.wildnest.databinding.ActivityOutputCameraBinding
import com.example.wildnest.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class OutputCamera : AppCompatActivity() {
    private lateinit var binding: ActivityOutputCameraBinding
    private var currentImageUri: Uri? = null
    private lateinit var bitmap: Bitmap
    private lateinit var info: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOutputCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fileInfo = "info.txt"
        val inputStringInfo = application.assets.open(fileInfo).bufferedReader().use {
            it.readText()
        }
        info = inputStringInfo.split("===")

        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.btnFindOut.setOnClickListener { findOut() }
    }
    private fun startCameraX() {
        val intent = Intent(this, CameraScreen::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun findOut() {
        val resized: Bitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, true)

        val model = Model.newInstance(this)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 128, 128, 3), DataType.FLOAT32)

        val pixelValues = FloatArray(128 * 128 * 3)
        var pixel = 0

        for (x in 0 until 128) {
            for (y in 0 until 128) {
                val px = resized.getPixel(x, y)

                pixelValues[pixel++] = ((px shr 16 and 0xFF) / 255.0).toFloat()
                pixelValues[pixel++] = ((px shr 8 and 0xFF) / 255.0).toFloat()
                pixelValues[pixel++] = ((px and 0xFF) / 255.0).toFloat()
            }
        }

        inputFeature0.loadArray(pixelValues, intArrayOf(1, 128, 128, 3))

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val max = getMax(outputFeature0.floatArray)
        binding.tvOutput.text = info[max]

        model.close()
    }

    fun getMax(arr: FloatArray): Int {
        var ind = 0
        var min = 0.0f

        for (i in 0..5) {
            if (arr[i] > min) {
                ind = i
                min = arr[i]
            }
        }
        return ind
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraScreen.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let { uri ->
            binding.previewImage.setImageURI(uri)
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        }
    }
}