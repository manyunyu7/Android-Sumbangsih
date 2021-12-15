package com.feylabs.sumbangsih.presentation.ktp_verif

import android.net.Uri
import android.os.Bundle
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
import com.feylabs.sumbangsih.presentation.ktp_verif.model_json.VerifNIKHelper


class KTPVerifStep4Fragment : BaseFragment() {

    val viewModel: KTPVerifViewModel by viewModel()

    var _binding: KtpVerifStep4FragmentBinding? = null
    val binding get() = _binding as KtpVerifStep4FragmentBinding

    private var objVerif: KTPVerifReq? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = KtpVerifStep4FragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        objVerif = VerifNIKHelper.getKTPVerifReq(requireContext())

        val photoUri: Uri? = arguments?.getString("face_uri")?.toUri()

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
                            objVerif?.photo_face = bitmapImage!!
                        }
                    })

                binding.btnNext.setOnClickListener {
                    VerifNIKHelper.savePref(requireContext(),objVerif)
                    findNavController().navigate(
                        R.id.action_nav_KTPVerifStep4Fragment_to_KTPVerifStep5Fragment,
                    )
                }


                tvDesc.text =
                    "Pastikan Foto KTP Memenuhi Frame dan dapat dibaca dengan jelas"
                imgFace.setImageURI(photoUri)
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
                bundleOf("type" to "verifnik_step4")
            )
        }

        binding.btnPhotoAgain.setOnClickListener {
            findNavController().navigate(
                R.id.nav_take_photo_fragment,
                bundleOf("type" to "verifnik_step4")
            )
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}




