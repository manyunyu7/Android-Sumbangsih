package com.feylabs.sumbangsih.presentation.pin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.RegisterRes
import com.feylabs.sumbangsih.databinding.FragmentVerifPinBinding
import com.feylabs.sumbangsih.presentation.CommonControllerActivity
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.DialogUtils
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import org.koin.android.viewmodel.ext.android.viewModel


class VerifPinFragment : BaseFragment() {

    var _binding: FragmentVerifPinBinding? = null
    val binding get() = _binding as FragmentVerifPinBinding

    val viewModel: AuthViewModel by viewModel()

    var numberFromServer = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVerifPinBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun initObserver() {
        viewModel.regNumberLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ManyunyuRes.Success -> {
                    setLoadingNumber(false)
                    proceedLogin(it.data)
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = "Success",
                        message = "Akun Anda Berhasil Dibuat",
                        positiveAction = Pair(getString(R.string.dialog_ok), {
                            proceedLogin(it.data)
                        }),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
                is ManyunyuRes.Error -> {
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = "Login Tidak Berhasil",
                        message = "Periksa kembali username dan password anda, atau coba kembali nanti",
                        positiveAction = Pair(getString(R.string.dialog_ok), {
                        }),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                    setLoadingNumber(false)
                    showToast("Gagal Membuat Akun", true)
                }
                is ManyunyuRes.Loading -> {
                    setLoadingNumber(true)
                }
                is ManyunyuRes.Empty -> {
                    setLoadingNumber(false)
                    showToast("Gagal Membuat Akun", true)
                }
            }
        })
    }

    private fun proceedLogin(data: RegisterRes?) {
        RazPreferenceHelper.saveUserId(requireContext(), data?.resData?.id.toString())
        RazPreferenceHelper.savePhoneNumber(requireContext(), data?.resData?.number.toString())
        RazPreferenceHelper.setLoggedIn(requireContext())
        findNavController().navigate(R.id.navigation_home)
    }

    private fun setLoadingNumber(b: Boolean) {
        if (b) {
            binding.includeShimmerPage.root.makeViewVisible()
        } else {
            binding.includeShimmerPage.root.makeViewGone()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (getActivity() as CommonControllerActivity).hideCustomTopbar()
        initObserver()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnNext.setOnClickListener {
            val text = binding.passCodeView.passCodeText.toString()
            val oldText = arguments?.getString("firstPin") ?: ""

            if (text != oldText) {
                "Pin Tidak Sesuai".showLongToast()
            } else {
                viewModel.registerNumber(RazPreferenceHelper.getPhoneNumber(requireContext()), text)
            }
        }
    }

}