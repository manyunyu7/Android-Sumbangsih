package com.feylabs.sumbangsih.presentation.change_credential

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.RegisterRes
import com.feylabs.sumbangsih.databinding.FragmentCheckPinBinding
import com.feylabs.sumbangsih.databinding.FragmentProceedPinBinding
import com.feylabs.sumbangsih.databinding.FragmentVerifPinBinding
import com.feylabs.sumbangsih.presentation.CommonControllerActivity
import com.feylabs.sumbangsih.presentation.pin.AuthViewModel
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.CommonHelper.logout
import com.feylabs.sumbangsih.util.DialogUtils
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import org.koin.android.viewmodel.ext.android.viewModel


class ProceedPinFragment : BaseFragment() {

    var _binding: FragmentProceedPinBinding? = null
    val binding get() = _binding as FragmentProceedPinBinding

    val viewModel: AuthViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProceedPinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (getActivity() as CommonControllerActivity).hideCustomTopbar()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnNext.makeViewGone()

        binding.passCodeViewz.setOnTextChangeListener {
            if (it.length == 6) {
                findNavController().navigate(
                    R.id.changePinFragment,
                    bundleOf("old_pin" to binding.passCodeViewz.passCodeText.toString())
                )
            }
        }

    }

}