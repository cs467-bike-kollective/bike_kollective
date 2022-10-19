package com.example.bikekollective

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.core.content.ContextCompat
import com.example.bikekollective.databinding.ActivityCameraBinding
import com.example.bikekollective.databinding.ActivityMainBinding
import java.io.File
import java.util.concurrent.ExecutorService

typealias LumaListener = (luma: Double) -> Unit
class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private var destination: Int? = null
    private lateinit var outPutDirectory: File

    companion object {
        private const val TAG = "CameraXInfo"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 2022
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private val FROM_ADD_BIKE_CODE = 2000

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        destination = intent.getIntExtra("identifier", 23)

    }

}