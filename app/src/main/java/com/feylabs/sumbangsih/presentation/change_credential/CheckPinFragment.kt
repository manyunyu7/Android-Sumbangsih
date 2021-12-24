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
import com.feylabs.sumbangsih.databinding.FragmentCheckPinBinding
import com.feylabs.sumbangsih.databinding.FragmentVerifPinBinding
import com.feylabs.sumbangsih.presentation.CommonControllerActivity
import com.feylabs.sumbangsih.presentation.pin.AuthViewModel
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.DialogUtils
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import org.koin.android.viewmodel.ext.android.viewModel


class CheckPinFragment : BaseFragment() {

    var _binding: FragmentCheckPinBinding? = null
    val binding get() = _binding as FragmentCheckPinBinding

    val viewModel: AuthViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCheckPinBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun initObserver() {
        viewModel.fireChangePass()
        viewModel.checkPinLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ManyunyuRes.Success -> {
                    showFullscreenLoading(false)
                    viewModel.fireCheckPin()
                    val data = it.data
                    data?.let { datas ->
                        if (datas.code == 200) {
                            viewModel.fireCheckPin()
                            gotoNext()
                        } else {
                            DialogUtils.showCustomDialog(
                                context = requireContext(),
                                title = "Perhatian",
                                message = "Password lama yang anda masukkan salah",
                                positiveAction = Pair(getString(R.string.dialog_ok), {
                                    viewModel.fireCheckPin()
                                }),
                                autoDismiss = true,
                                buttonAllCaps = false
                            )
                        }
                    }
                    viewModel.fireCheckPin()
                }
                is ManyunyuRes.Error -> {
                    showFullscreenLoading(false)
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = "Perhatian",
                        message = "Password lama yang anda masukkan salah",
                        positiveAction = Pair(getString(R.string.dialog_ok), {
                            viewModel.fireCheckPin()
                        }),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                    viewModel.fireCheckPin()
                }
                is ManyunyuRes.Loading -> {
                    showFullscreenLoading(true)
                }
                is ManyunyuRes.Empty -> {
                    showFullscreenLoading(false)
                    viewModel.fireCheckPin()
                    showToast("Gagal Membuat Akun", true)
                }
                is ManyunyuRes.Default -> {
                }
            }
        })
    }

    private fun gotoNext() {
        findNavController().navigate(R.id.action_checkPinFragment_to_proceedPinFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (getActivity() as CommonControllerActivity).hideCustomTopbar()
        initObserver()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.passCodeView.setOnTextChangeListener {
            if (it.length == 6) {
                viewModel.checkPassword(
                    userId = RazPreferenceHelper.getUserId(requireContext()),
                    password = binding.passCodeView.passCodeText.toString()
                )
            }
        }

        binding.btnNext.makeViewGone()
    }

}