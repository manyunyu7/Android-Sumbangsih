package com.feylabs.sumbangsih.presentation.ui.pin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.FragmentCreatePinBinding
import com.feylabs.sumbangsih.util.BaseFragment


class CreatePinFragment : BaseFragment() {

    var _binding: FragmentCreatePinBinding? = null
    val binding get() = _binding as FragmentCreatePinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreatePinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGoToVerif.setOnClickListener {
            val text = binding.passCodeView.passCodeText.toString().length
            if (text < 6) {
                "Lengkapi Inputan".showLongToast()
            } else {
                findNavController().navigate(
                    R.id.verifPinFragment, bundleOf(
                        "firstPin" to binding.passCodeView.passCodeText
                    )
                )
            }
        }
    }

}