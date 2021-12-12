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
import com.feylabs.sumbangsih.util.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class KTPVerifStep4Fragment : BaseFragment() {

    val viewModel: KTPVerifViewModel by viewModel()

    var _binding: KtpVerifStep4FragmentBinding? = null
    val binding get() = _binding as KtpVerifStep4FragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = KtpVerifStep4FragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photoUri: Uri? = arguments?.getString("face_uri")?.toUri()

        if (photoUri != null) {
            binding.tvDesc.text = "Pastikan Foto KTP Memenuhi Frame dan dapat dibaca dengan jelas"
            binding.imgKtp.setImageURI(photoUri)
            binding.btnTakePhoto.makeViewGone()
            binding.containerPhotoTaken.makeViewVisible()
        } else {
            binding.tvDesc.text = getString(R.string.desc_take_photo_verif_ktp)
            binding.containerPhotoTaken.makeViewGone()
            binding.btnTakePhoto.makeViewVisible()
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

        binding.btnNext.setOnClickListener {

        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}




