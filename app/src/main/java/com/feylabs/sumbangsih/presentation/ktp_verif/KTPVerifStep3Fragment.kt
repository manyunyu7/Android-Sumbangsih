package com.feylabs.sumbangsih.presentation.ktp_verif

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.KtpVerifStep3FragmentBinding
import com.feylabs.sumbangsih.util.BaseFragment
import com.yalantis.ucrop.UCrop
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File

class KTPVerifStep3Fragment : BaseFragment() {

    val viewModel: KTPVerifViewModel by viewModel()

    var _binding: KtpVerifStep3FragmentBinding? = null
    val binding get() = _binding as KtpVerifStep3FragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = KtpVerifStep3FragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photoUri: Uri? = arguments?.getString("ktp_uri")?.toUri()

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
                bundleOf("type" to "verifnik_step3")
            )
        }

        binding.btnPhotoAgain.setOnClickListener {
            findNavController().navigate(
                R.id.nav_take_photo_fragment,
                bundleOf("type" to "verifnik_step3")
            )
        }

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_nav_KTPVerifStep3Fragment_to_KTPVerifStep4Fragment)
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}



