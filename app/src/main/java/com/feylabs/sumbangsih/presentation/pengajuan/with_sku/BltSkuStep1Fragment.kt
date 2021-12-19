package com.feylabs.sumbangsih.presentation.pengajuan.with_sku

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.KtpVerifStep4FragmentBinding
import com.feylabs.sumbangsih.presentation.ktp_verif.model_json.KTPVerifReq
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.ImageView
import org.koin.android.viewmodel.ext.android.viewModel
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.databinding.BltSkuStep1FragmentBinding
import com.feylabs.sumbangsih.presentation.ktp_verif.model_json.VerifNIKHelper
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import com.feylabs.sumbangsih.util.sharedpref.RazPreferences


class BltSkuStep1Fragment : BaseFragment() {


    var _binding: BltSkuStep1FragmentBinding? = null
    val binding get() = _binding as BltSkuStep1FragmentBinding

    private var objVerif: PengajuanSKU? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BltSkuStep1FragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idEvent = RazPreferences(requireContext()).getPrefString("event_id") ?: ""

        objVerif = PengajuanSKU(
            event_id = idEvent,
            user_id = RazPreferenceHelper.getUserId(requireContext())
        )

        val photoUri: Uri? = arguments?.getString("uri")?.toUri()
        val latSelfie = arguments?.getString("lat")
        val longSelfie = arguments?.getString("long")


        binding.apply {
            if (photoUri != null) {
                btnNext.isEnabled = true

                imgFace.viewTreeObserver
                    .addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            val width: Int = imgFace.getWidth()
                            val height: Int = imgFace.getHeight()
                            //you can add your code here on what you want to do to the height and width you can pass it as parameter or make width and height a global variable
                            imgFace.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            val bitmapImage = ImageView.convertViewToBase64(
                                binding.imgFace,
                                width = width,
                                height = height
                            )

                            objVerif?.photo_selfie = bitmapImage
                            if (latSelfie != null) {
                                objVerif!!.lat_selfie = latSelfie
                            }
                            if (longSelfie != null) {
                                objVerif!!.long_selfie = longSelfie
                            }
                        }
                    })



                binding.btnNext.setOnClickListener {
                    findNavController().navigate(R.id.action_nav_bltSkuStep1Fragment_to_bltSkuStep2Fragment)
                    PengajuanSKUObjectHelper.savePref(requireContext(), objVerif)
                }

                tvDesc.text =
                    "Pastikan Foto Selfie Bersama KTP memenuhi frame dan dapat dibaca dengan jelas"
                imgFace.setImageURI(photoUri)
                btnTakePhoto.makeViewGone()
                containerPhotoTaken.makeViewVisible()
            } else {
                btnNext.isEnabled = false

                tvDesc.text =
                    "Silahkan aktifan GPS dan lakukan foto selfie bersama KTP dari alamat sesuai KTP"
                containerPhotoTaken.makeViewGone()
                btnTakePhoto.makeViewVisible()
            }
        }


        binding.btnTakePhoto.setOnClickListener {
            findNavController().navigate(
                R.id.nav_take_photo_fragment,
                bundleOf("type" to "bltsku_step1")
            )
        }

        binding.btnPhotoAgain.setOnClickListener {
            findNavController().navigate(
                R.id.nav_take_photo_fragment,
                bundleOf("type" to "bltsku_step1")
            )
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}




