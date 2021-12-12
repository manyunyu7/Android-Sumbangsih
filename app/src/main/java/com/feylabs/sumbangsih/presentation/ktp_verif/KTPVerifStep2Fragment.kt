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
import org.koin.android.viewmodel.ext.android.viewModel

class KTPVerifStep2Fragment : Fragment() {

    val viewModel: KTPVerifViewModel by viewModel()

    var _binding: KtpVerifStep2FragmentBinding? = null
    val binding get() = _binding as KtpVerifStep2FragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = KtpVerifStep2FragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNegative.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnPositive.setOnClickListener {
            findNavController().navigate(R.id.action_KTPVerifStep2Fragment_to_KTPVerifStep3Fragment)
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}