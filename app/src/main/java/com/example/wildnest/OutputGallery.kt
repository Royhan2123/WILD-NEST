package com.example.wildnest

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.example.wildnest.databinding.ActivityOutputGalleryBinding
import com.example.wildnest.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException

@Suppress("DEPRECATION")
class OutputGallery : AppCompatActivity() {
    private lateinit var binding: ActivityOutputGalleryBinding
    // private var currentImageUri: Uri? = null
    private lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOutputGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // image processor
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
//            .add(NormalizeOp(0.0f,255.0f))
//            .add(TransformToGrayscaleOp())
            .build()

        binding.btnGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        binding.btnFindOut.setOnClickListener {
            val tensorImage = TensorImage(DataType.UINT8)
            tensorImage.load(bitmap)

            imageProcessor.process(tensorImage)

            val model = Model.newInstance(this)
            val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, 128, 128, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(tensorImage.buffer)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

            var maxIdx = 0
            outputFeature0.forEachIndexed { index, fl ->
                if (outputFeature0[maxIdx] < fl) {
                    maxIdx = index
                }
                binding.txtName.text = maxIdx.toString()
            }
            model.close()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == 100) {
            val uri = data?.data
            uri?.let {
                try {
                    // Load the selected image into the bitmap
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, it)

                    // Display the bitmap in previewImageGallery
                    binding.previewImageGallery.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
//    private fun startGallery() {
//        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//    }
//
//    private val launcherGallery = registerForActivityResult(
//        ActivityResultContracts.PickVisualMedia()
//    ) { uri: Uri? ->
//        if (uri != null) {
//            currentImageUri = uri
//            showImage()
//        } else {
//            Log.d("Photo Picker", "No media selected")
//        }
//    }
//    private fun showImage() {
//        binding.previewImageGallery.setImageURI(currentImageUri)
//    }
}
