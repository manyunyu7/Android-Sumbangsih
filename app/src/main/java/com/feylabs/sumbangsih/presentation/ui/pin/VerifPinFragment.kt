package com.feylabs.sumbangsih.presentation.ui.pin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.FragmentVerifPinBinding
import com.feylabs.sumbangsih.util.BaseFragment


class VerifPinFragment : BaseFragment() {

    var _binding: FragmentVerifPinBinding? = null
    val binding get() = _binding as FragmentVerifPinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVerifPinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNext.setOnClickListener {
            val text = binding.passCodeView.passCodeText.toString()
            val oldText = arguments?.getString("firstPin") ?: ""

            if (text != oldText) {
                "Pin Tidak Sesuai".showLongToast()
            } else {
                findNavController().navigate(
                    R.id.navigation_dashboard, bundleOf(
                        "firstPin" to binding.passCodeView.passCodeText
                    )
                )
            }
        }
    }

}