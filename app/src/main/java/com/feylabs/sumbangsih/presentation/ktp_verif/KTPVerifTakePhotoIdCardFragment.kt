package com.feylabs.sumbangsih.presentation.ktp_verif

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.KtpVerifTakeIdPhotoFragmentBinding
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.CommonHelper
import com.feylabs.sumbangsih.util.CommonHelper.REQUIRED_PERMISSIONS_PHOTO_KTP
import com.yalantis.ucrop.UCrop
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

    private var type: String? = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        type = arguments?.getString("type") ?: ""

        if (type != null) {
            when (type) {
                "bltnonsku_step1" -> {
                    binding.tvContent.text = "Mohon atur posisi objek agar terdeteksi"
//                    Glide.with(this).asGif().load(R.raw.sbg_phone_rotation)
//                        .into(binding.ivCropperFrame)
                    binding.ivCropperFrame.makeViewGone()
                    hideSuggestion()
                }
                "verifnik_step3" -> {
                    binding.tvContent.text = getString(R.string.desc_photo_frame_verif_nik_step3)
                    binding.ivCropperFrame.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_circa_ktp
                        )
                    )
                }
                "verifnik_step4" -> {
                    binding.tvContent.text = getString(R.string.desc_photo_frame_verif_nik_step4)
                    binding.ivCropperFrame.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_circa_face
                        )
                    )
                }
            }
        }

        outputDirectory = getOutputDirectory()

        binding.btnTakePhoto.setOnClickListener {
            takePhoto()
        }

        if (allPermissionGranted()) {
            startCamera()
        } else {
            showToast("Permission Requested")
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS_PHOTO_KTP,
                CommonHelper.REQUEST_CODE_PERMISSION
            )
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun hideSuggestion() {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            binding.ivCropperFrame.makeViewGone()
        }, 3000)

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
        imageCapture.targetRotation = Surface.ROTATION_90

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
                    startCropping(savedUri)
                    showToast("Gambar Berhasil Diambil")
                }

                override fun onError(p0: ImageCaptureException) {
                    showToast("Gambar Gagal Disimpan")
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

            var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            when (type) {
                "verifnik_step3" -> {
                    cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                }
                "verifnik_step4" -> {
                    cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                }
            }

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
        REQUIRED_PERMISSIONS_PHOTO_KTP.all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            val imageResCrop = UCrop.getOutput(data!!)
            if (imageResCrop != null) {
                when (type) {
                    "verifnik_step3" -> {
                        findNavController().navigate(
                            R.id.nav_KTPVerifStep3Fragment,
                            bundleOf("ktp_uri" to imageResCrop.toString())
                        )
                    }
                    "verifnik_step4" -> {
                        findNavController().navigate(
                            R.id.nav_KTPVerifStep4Fragment,
                            bundleOf("face_uri" to imageResCrop.toString())
                        )
                    }
                    "bltnonsku_step1" -> {
                        findNavController().navigate(
                            R.id.bltWithoutSkuStep1Fragment,
                            bundleOf("uri" to imageResCrop.toString())
                        )
                    }
                }

            }
        }
    }

    private fun startCropping(uri: Uri) {
        val destName = System.currentTimeMillis().toString() + ".jpg"
        val uCrop = UCrop.of(uri, Uri.fromFile(File(requireContext().cacheDir, destName)))

        UCrop.of(uri, Uri.fromFile(File(context?.cacheDir, destName)))
            .withAspectRatio(1.toFloat(), 1.toFloat())
            .withOptions(getOptions())
            .start(requireContext(), this)
    }

    private fun getOptions(): UCrop.Options {
        val options = UCrop.Options()
        options.setCompressionQuality(70)
        options.setFreeStyleCropEnabled(true)
        options.setToolbarWidgetColor(
            ContextCompat.getColor(
                requireContext() as Activity,
                R.color.white
            )
        )
        options.setToolbarColor(
            ContextCompat.getColor(
                requireContext() as Activity,
                R.color.redSumbangsih
            )
        )
        options.setToolbarTitle("Edit Photo")
        return options
    }


}