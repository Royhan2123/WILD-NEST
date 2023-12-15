package com.example.wildnest.Gallery

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.example.wildnest.databinding.ActivityOutputGalleryBinding
import com.example.wildnest.ml.MobilenetV110224Quant
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
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

        val fileName = "labels.txt"
        val inputString = application.assets.open(fileName).bufferedReader().use {
            it.readText()
        }
        val townList = inputString.split("\n")

        binding.btnGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        binding.btnFindOut.setOnClickListener {
            val resized : Bitmap = Bitmap.createScaledBitmap(bitmap,224,224,true)

            val model = MobilenetV110224Quant.newInstance(this)

            val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)

            val tbuffer = TensorImage.fromBitmap(resized)
            val byteBuffer = tbuffer.buffer
            inputFeature0.loadBuffer(byteBuffer)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            val max = getMax(outputFeature0.floatArray)
            binding.tvOutput.text = townList[max]
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

        for (i in 0..1000) {
            if (arr[i] > min) {
                ind = i
                min = arr[i]
            }
        }
        return ind
    }
}