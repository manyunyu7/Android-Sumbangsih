package com.feylabs.sumbangsih.presentation.ktp_verif

import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.KtpVerifStep4FragmentBinding
import com.feylabs.sumbangsih.databinding.KtpVerifStep5FragmentBinding
import com.feylabs.sumbangsih.presentation.ktp_verif.model_json.KTPVerifReq
import com.feylabs.sumbangsih.presentation.ktp_verif.model_json.VerifNIKHelper
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.ImageView
import org.koin.android.viewmodel.ext.android.viewModel

class KTPVerifStep5Fragment : BaseFragment() {

    val viewModel: KTPVerifViewModel by viewModel()

    var _binding: KtpVerifStep5FragmentBinding? = null
    val binding get() = _binding as KtpVerifStep5FragmentBinding

    private var objVerif: KTPVerifReq? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = KtpVerifStep5FragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        objVerif = VerifNIKHelper.getKTPVerifReq(requireContext())
        val imageKtp: ByteArray = Base64.decode(objVerif?.photo_requested, Base64.DEFAULT)
        val imageFace: ByteArray = Base64.decode(objVerif?.photo_face, Base64.DEFAULT)

        Glide.with(requireContext())
            .asBitmap()
            .load(imageKtp)
            .into(binding.imgKtp)

        Glide.with(requireContext())
            .asBitmap()
            .load(imageFace)
            .into(binding.imgFace)

        binding.tvTitle.text = "NIK : " + objVerif?.nik

        binding.btnNext.setOnClickListener {

        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}




