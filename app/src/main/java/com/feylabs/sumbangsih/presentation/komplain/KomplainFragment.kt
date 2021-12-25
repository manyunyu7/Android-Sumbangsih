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
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.FragmentKomplainBinding
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.UiUtils.addCurrencyTextWatcher
import com.yalantis.ucrop.UCrop
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File


class KomplainFragment : BaseFragment() {

    private var _binding: FragmentKomplainBinding? = null
    val binding get() = _binding as FragmentKomplainBinding

    private val viewModel : KomplainViewModel by viewModel()

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

    private fun initUi() {

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
                binding.ivBukti.setImageURI(null)
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
                binding.ivBuktiPcl.makeViewVisible()

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
        binding.ivBuktiPcl.makeViewVisible()
        binding.ivBukti.setImageURI(null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
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
                binding.ivBuktiPcl.makeViewGone()
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


}