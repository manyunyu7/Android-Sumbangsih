package com.feylabs.sumbangsih.presentation.change_credential

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.RegisterRes
import com.feylabs.sumbangsih.databinding.FragmentChangePinBinding
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


class ChangePinFragment : BaseFragment() {

    var _binding: FragmentChangePinBinding? = null
    val binding get() = _binding as FragmentChangePinBinding

    val viewModel: AuthViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChangePinBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun initObserver() {
        viewModel.changePwLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ManyunyuRes.Success -> {
                    showFullscreenLoading(false)
                    val data = it.data
                    data?.let {
                        if (it.apiCode == 1) {
                            DialogUtils.showCustomDialog(
                                context = requireContext(),
                                title = "Success",
                                message = "Penggantian PIN berhasil, Silakan untuk melakukan login kembali",
                                positiveAction = Pair(getString(R.string.dialog_ok), {
                                    viewModel.fireChangePass()
                                    logout()
                                }),
                                autoDismiss = true,
                                buttonAllCaps = false
                            )
                        } else {
                            DialogUtils.showCustomDialog(
                                context = requireContext(),
                                title = "Error",
                                message = "Terjadi Kesalahan Saat Mengganti Password",
                                positiveAction = Pair(getString(R.string.dialog_ok), {}),
                                autoDismiss = true,
                                buttonAllCaps = false
                            )
                        }
                    }
                    viewModel.fireChangePass()
                }
                is ManyunyuRes.Error -> {
                    showFullscreenLoading(false)
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = "Error",
                        message = "Terjadi Kesalahan Saat Mengganti Password",
                        positiveAction = Pair(getString(R.string.dialog_ok), {}),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                    viewModel.fireChangePass()
                }
                is ManyunyuRes.Loading -> {
                    showFullscreenLoading(true)
                }
                is ManyunyuRes.Empty -> {
                    showFullscreenLoading(false)
                    viewModel.fireCheckPin()
                    showToast("Gagal Membuat Akun", true)
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (getActivity() as CommonControllerActivity).hideCustomTopbar()

        initObserver()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnNext.setOnClickListener {
            val oldPin = arguments?.getString("old_pin") ?: ""
            val newPin = binding.passCodeView.passCodeText

            if (oldPin != newPin) {
                DialogUtils.showCustomDialog(
                    context = requireContext(),
                    title = "Perhatian",
                    message = "Pin Tidak Sama",
                    positiveAction = Pair(getString(R.string.dialog_ok), {
                    }),
                    autoDismiss = true,
                    buttonAllCaps = false
                )
            } else {
                viewModel.updatePasswordCompact(
                    RazPreferenceHelper.getUserId(requireContext()),
                    newPin
                )
            }
        }
    }
}