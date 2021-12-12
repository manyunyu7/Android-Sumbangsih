package com.feylabs.sumbangsih.presentation.ktp_verif

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.KtpVerifStep1FragmentBinding
import com.feylabs.sumbangsih.util.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class KTPVerifStep1Fragment : BaseFragment() {

    val viewModel: KTPVerifViewModel by viewModel()

    var _binding: KtpVerifStep1FragmentBinding? = null
    val binding get() = _binding as KtpVerifStep1FragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = KtpVerifStep1FragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNext.isEnabled = false

        binding.inputMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                binding.btnNext.isEnabled = p0.length >= 16
            }

            override fun afterTextChanged(p0: Editable?) {


            }

        })

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_KTPVerifStep1Fragment_to_KTPVerifStep2Fragment)
        }
    }


}