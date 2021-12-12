package com.feylabs.sumbangsih.presentation.ktp_verif

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.KtpVerifTakeIdPhotoFragmentBinding
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.CommonHelper
import com.feylabs.sumbangsih.util.CommonHelper.REQUIRED_PERMISSIONS
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class KTPVerifTakePhotoIdCardFragment : BaseFragment() {

    val viewModel: KTPVerifViewModel by viewModel()

    private var imageCapture: ImageCapture? = null
    private var outputDirectory: File? = null

    var _binding: KtpVerifTakeIdPhotoFragmentBinding? = null
    val binding get() = _binding as KtpVerifTakeIdPhotoFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = KtpVerifTakeIdPhotoFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        outputDirectory = getOutputDirectory()

        binding.btnTakePhoto.setOnClickListener {
            takePhoto()
        }

        if (allPermissionGranted()) {
            startCamera()
        } else {
            showToast("Permission Requested")
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS,
                CommonHelper.REQUEST_CODE_PERMISSION
            )
        }


        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getOutputDirectory(): File? {
        return if (activity != null) {
            val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let { mFile ->
                File(mFile, resources.getString(R.string.app_name)).apply {
                    mkdirs()
                }
            }

            return if (mediaDir != null && mediaDir.exists())
                mediaDir else activity?.filesDir
        } else null
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                CommonHelper.FILE_NAME_FORMAT,
                Locale.getDefault()
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOption,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(p0: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo Saved"
                }

                override fun onError(p0: ImageCaptureException) {
                }

            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val previewView = Preview.Builder()
                .build()
                .also { mPreview ->
                    mPreview.setSurfaceProvider(
                        binding.viewFinder.surfaceProvider
                    )
                }

            imageCapture = ImageCapture.Builder()
                .build()

//            binding.btnFlashlight.setOnClickListener {
//                Toast.makeText(this, "Fl", Toast.LENGTH_SHORT).show()
//                if (imageCapture?.flashMode == ImageCapture.FLASH_MODE_OFF ||
//                    imageCapture?.flashMode == ImageCapture.FLASH_MODE_AUTO
//                ) {
//                    imageCapture?.flashMode = ImageCapture.FLASH_MODE_ON
//                }
//
//                if (imageCapture?.flashMode == ImageCapture.FLASH_MODE_ON) {
//                    imageCapture?.flashMode = ImageCapture.FLASH_MODE_OFF
//                }
//
//            }


            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                val cam: Camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, previewView, imageCapture
                )

//                binding.btnFlashlight.setOnClickListener {
//                    if (cam.getCameraInfo().hasFlashUnit()) {
//
//                        when(cam.cameraInfo.torchState.value){
//                            TorchState.ON->{
//                                cam.getCameraControl().enableTorch(false) // or false
//                            }
//                            TorchState.OFF->{
//                                cam.getCameraControl().enableTorch(true) // or false
//                            }
//                        }
//
//                    }
//                }

            } catch (e: Exception) {
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CommonHelper.REQUEST_CODE_PERMISSION) {
            if (allPermissionGranted()) {
                startCamera()
            } else {
            }
        }
    }

    private fun allPermissionGranted(): Boolean =
        CommonHelper.REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }


}