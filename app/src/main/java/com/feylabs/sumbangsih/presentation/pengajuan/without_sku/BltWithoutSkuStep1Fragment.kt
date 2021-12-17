package com.feylabs.sumbangsih.presentation.pengajuan.without_sku

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.BltWithoutSkuStep1FragmentBinding
import com.feylabs.sumbangsih.databinding.KtpVerifStep3FragmentBinding
import com.feylabs.sumbangsih.presentation.ktp_verif.model_json.KTPVerifReq
import com.feylabs.sumbangsih.presentation.ktp_verif.model_json.VerifNIKHelper
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.ImageView
import com.feylabs.sumbangsih.util.ImageView.convertViewToBase64
import com.feylabs.sumbangsih.util.sharedpref.RazPreferences
import com.google.gson.Gson
import com.yalantis.ucrop.UCrop
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File

class BltWithoutSkuStep1Fragment : BaseFragment() {


    var _binding: BltWithoutSkuStep1FragmentBinding? = null
    val binding get() = _binding as BltWithoutSkuStep1FragmentBinding

    private var objVerif: KTPVerifReq? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BltWithoutSkuStep1FragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photoUri: Uri? = arguments?.getString("uri")?.toUri()

        objVerif = VerifNIKHelper.getKTPVerifReq(requireContext())

        binding.apply {

            if (photoUri != null) {
                btnNext.isEnabled = true
                imgKtp.viewTreeObserver
                    .addOnGlobalLayoutListener(object :
                        ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            val width: Int = imgKtp.getWidth()
                            val height: Int = imgKtp.getHeight()
                            //you can add your code here on what you want to do to the height and width you can pass it as parameter or make width and height a global variable
                            imgKtp.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            val bitmapImage = ImageView.convertViewToBase64(
                                binding.imgKtp,
                                width = width,
                                height = height
                            )
                            objVerif?.photo_requested = bitmapImage!!
                        }
                    })

                binding.btnNext.setOnClickListener {
                    Timber.d("STEP_BLT_WITHOUT_SKU_1 (next) ${objVerif.toString()}")
                    VerifNIKHelper.savePref(requireContext(), objVerif)
                    findNavController().navigate(
                        R.id.action_nav_KTPVerifStep3Fragment_to_KTPVerifStep4Fragment,
                    )
                }

                tvDesc.text =
                    "Pastikan Foto KTP Memenuhi Frame dan dapat dibaca dengan jelas"
                imgKtp.setImageURI(photoUri)
                btnTakePhoto.makeViewGone()
                containerPhotoTaken.makeViewVisible()
            } else {
                btnNext.isEnabled = false
                tvDesc.text = getString(R.string.desc_take_photo_verif_ktp)
                containerPhotoTaken.makeViewGone()
                btnTakePhoto.makeViewVisible()
            }
        }

        binding.btnTakePhoto.setOnClickListener {
            findNavController().navigate(
                R.id.nav_take_photo_fragment,
                bundleOf("type" to "bltnonsku_step1")
            )
        }

        binding.btnPhotoAgain.setOnClickListener {
            findNavController().navigate(
                R.id.nav_take_photo_fragment,
                bundleOf("type" to "bltnonsku_step1")
            )
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}




