package com.example.wildnest.gallery

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.example.wildnest.databinding.ActivityOutputGalleryBinding
import com.example.wildnest.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

@Suppress("DEPRECATION")
class OutputGallery : AppCompatActivity() {
    private lateinit var binding: ActivityOutputGalleryBinding
    private lateinit var bitmap: Bitmap

    @SuppressLint("IntentReset")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOutputGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fileInfo = "info.txt"
        val inputStringInfo = application.assets.open(fileInfo).bufferedReader().use {
            it.readText()
        }
        val info = inputStringInfo.split("===")

        binding.btnGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        binding.btnFindOut.setOnClickListener {
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
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.previewImage.setImageURI(data?.data)
        val uri: Uri? = data?.data
        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
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
}