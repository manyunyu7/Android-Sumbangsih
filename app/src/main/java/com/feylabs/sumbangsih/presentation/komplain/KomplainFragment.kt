package com.feylabs.sumbangsih.presentation.komplain

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.databinding.FragmentKomplainBinding
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.DialogUtils
import com.feylabs.sumbangsih.util.ImageView
import com.feylabs.sumbangsih.util.ObjectHelperCommon.serializeToMap
import com.feylabs.sumbangsih.util.UiUtils.addCurrencyTextWatcher
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import com.yalantis.ucrop.UCrop
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


class KomplainFragment : BaseFragment() {

    private var _binding: FragmentKomplainBinding? = null
    val binding get() = _binding as FragmentKomplainBinding

    private val viewModel: KomplainViewModel by viewModel()

    private val REQUEST_IMAGE_GALLERY = 2
    private val PERMISSION_CODE_STORAGE = 1001

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKomplainBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initObserver() {
        viewModel.uploadPengajuanVm.observe(viewLifecycleOwner, {
            when (it) {
                is ManyunyuRes.Default -> {
                    showFullscreenLoading(false)
                }
                is ManyunyuRes.Empty -> {
                    showFullscreenLoading(false)
                }
                is ManyunyuRes.Error -> {
                    showToast(it.message.toString())
                    showFullscreenLoading(false)
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = "Gagal",
                        message = "Terjadi Kesalahan ketika mengupload data : ${it.message}",
                        positiveAction = Pair(getString(R.string.dialog_ok), {}),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
                is ManyunyuRes.Loading -> {
                    showFullscreenLoading(true)
                }
                is ManyunyuRes.Success -> {
                    showSuccess()
                    showToast(it.message.toString())
                    showFullscreenLoading(false)
                    viewModel.fireUploadVerifVM()
                }
            }
        })

    }

    private fun showSuccess() {
        binding.includeSuccess.root.makeViewVisible()
        binding.includeSuccess.btnAction.setOnClickListener {
            findNavController().popBackStack(R.id.navigation_home, true)
        }
    }


    private fun initUi() {

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.includeSuccess.btnAction.setOnClickListener {
            findNavController().navigate(R.id.navigation_home)
        }

        binding.btnSubmit.setOnClickListener {
            submit()
        }

        binding.ivBukti.setOnClickListener {
            pickPhoto()
        }

        binding.etJmlDana.addCurrencyTextWatcher()

        binding.apply {
            val listForm = arrayOf(
                containerPhoto, etFeedback, etRejectionAt, etKomplainDana, labelUpload
            )
            listForm.forEachIndexed { index, view ->
                view.makeViewGone()
            }

        }

        setDropdownAdapter(
            binding.dropdownType, requireContext(),
            arrayOf("Dana", "Waktu", "Penolakan")
        )

        binding.dropdownType.setOnItemClickListener { adapterView, view, i, l ->
            setupViewWithType(i)
        }

        setDropdownAdapter(
            binding.dropdownRejectionAt, requireContext(),
            arrayOf("Kecamatan", "Kelurahan", "Panitia")
        )

        setDropdownAdapter(
            binding.dropdownKomplainDana, requireContext(),
            arrayOf("Kelebihan", "Kekurangan", "Lain-lain")
        )

    }

    private fun submit() {
        binding.apply {

            GlobalScope.launch {

                var bitmapImage: String? = null

                val obj = KomplainRequestBody(
                    type = binding.dropdownType.text.toString(),
                    user_id = RazPreferenceHelper.getUserId(requireContext()),
                    dana_option = binding.dropdownKomplainDana.text.toString(),
                    dana_excess = binding.etJmlDana.editText?.text.toString(),
                    rejected_at = binding.etRejectionAt.editText?.text.toString(),
                    feedback = binding.etFeedback.editText?.text.toString(),
                    photo = uriToBase64(viewModel.imageUriVm.value),
                    notes = null
                )

                viewModel.upload(
                    obj.serializeToMap()
                )
            }

        }
    }

    private fun pickPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_DENIED
            ) {
                val permission = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                requestPermissions(permission, PERMISSION_CODE_STORAGE)
            } else {
                clickGallery()
            }
        } else {
            clickGallery()
        }
    }

    private fun clickGallery() {
        showToast("Click Gallery")
        val galleryIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        try {
            startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY)
        } catch (e: ActivityNotFoundException) {
            showToast("Error $e")
            error() { "No Activity Found" }
        }
    }

    private fun setupViewWithType(i: Int) {
        clearSelection()
        when (i) {
            0 -> {
                // if dana
                binding.ivBukti.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_choose_bukti
                    )
                )
                binding.etRejectionAt.makeViewGone()
                binding.etFeedback.makeViewGone()

                binding.labelUpload.makeViewVisible()

                binding.etJmlDana.makeViewVisible()
                binding.etKomplainDana.makeViewVisible()
                binding.containerPhoto.makeViewVisible()

            }
            1 -> {
                // if waktu
                binding.ivBukti.setImageURI(null)

                binding.labelUpload.makeViewGone()
                binding.containerPhoto.makeViewGone()
                binding.etJmlDana.makeViewGone()
                binding.etKomplainDana.makeViewGone()
                binding.etRejectionAt.makeViewGone()

                binding.etFeedback.makeViewVisible()
            }
            2 -> {
                // penolakan
                binding.ivBukti.setImageURI(null)
                binding.containerPhoto.makeViewGone()
                binding.etJmlDana.makeViewGone()
                binding.etKomplainDana.makeViewGone()
                binding.labelUpload.makeViewGone()

                binding.etRejectionAt.makeViewVisible()
                binding.etFeedback.makeViewVisible()
            }
        }
    }

    private fun clearSelection() {
        binding.etJmlDana.editText?.text?.clear()
        binding.etRejectionAt.editText?.text?.clear()
        binding.etKomplainDana.editText?.text?.clear()
        binding.ivBukti.setImageURI(null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObserver()
    }

    fun setDropdownAdapter(view: AutoCompleteTextView, context: Context, list: Array<String>) {
        val adapter = ArrayAdapter(
            context, R.layout.item_dropdown_list,
            list
        )
        view.setAdapter(adapter)
        view.threshold = 100
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                data.data?.let {
                    startCropping(it)
//                    uploadPhotoFromGallery(it)
                } ?: error { "Error when pick image from gallery" }
            }
        }

        if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            val imageResCrop = UCrop.getOutput(data!!)
            if (imageResCrop != null) {
                viewModel.imageUriVm.value = imageResCrop
                binding.ivBukti.setImageURI(imageResCrop)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    for (result in grantResults) {
                        if (result == PackageManager.PERMISSION_DENIED) {
                            return
                        }
                    }
                    clickGallery()
                }
            }
        }
    }


    private suspend fun uriToBase64(uri: Uri?): String? {
        if (uri == null) {
            showToast("Terjadi Kesalahan Saat Mengupload Foto")
            throw error("Uri is null")
        } else {
            try {
                withContext(Dispatchers.IO) {
                    var bitmap: Bitmap? = null
                    bitmap = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(
                            activity?.contentResolver,
                            uri
                        )
                    } else {
                        val source = ImageDecoder.createSource(activity?.contentResolver!!, uri!!)
                        ImageDecoder.decodeBitmap(source)
                    }

                    val outputStream = ByteArrayOutputStream()
                    bitmap?.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
                    val byteArray: ByteArray = outputStream.toByteArray()

                    //Use your Base64 String as you wish
                    val encodedString: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
                    return@withContext encodedString
                }


            } catch (e: Exception) {
                showToast("Terjadi Kesalahan : $e")
                throw error(e.message.toString())
            }
        }
        return null
    }


}