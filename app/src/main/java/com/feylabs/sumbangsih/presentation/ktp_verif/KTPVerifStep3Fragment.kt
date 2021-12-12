package com.feylabs.sumbangsih.presentation.ktp_verif

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.KtpVerifStep2FragmentBinding
import com.feylabs.sumbangsih.databinding.KtpVerifStep3FragmentBinding
import org.koin.android.ext.android.bind
import org.koin.android.viewmodel.ext.android.viewModel

class KTPVerifStep3Fragment : Fragment() {

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

        binding.btnTakePhoto.setOnClickListener {
            findNavController().navigate(R.id.action_KTPVerifStep3Fragment_to_KTPVerifTakePhotoIdCardFragment)
        }

        binding.btnPhotoAgain.setOnClickListener {
            findNavController().navigate(R.id.action_KTPVerifStep3Fragment_to_KTPVerifTakePhotoIdCardFragment)
        }

        binding.btnNext.setOnClickListener {

        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}