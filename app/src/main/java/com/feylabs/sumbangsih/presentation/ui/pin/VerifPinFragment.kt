package com.feylabs.sumbangsih.presentation.ui.pin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.databinding.FragmentVerifPinBinding
import com.feylabs.sumbangsih.util.BaseFragment
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
                    proceedLogin(it.data.toString())
                    showToast("Berhasil Membuat Akun")
                }
                is ManyunyuRes.Error -> {
                    showToast("Gagal Membuat Akun", true)
                }
                is ManyunyuRes.Loading -> {
                    showToast("Loading")
                }
                is ManyunyuRes.Empty -> {
                    showToast("Gagal Membuat Akun", true)
                }
            }
        })
    }

    private fun proceedLogin(number: String) {
        RazPreferenceHelper.savePhoneNumber(requireContext(), number)
        RazPreferenceHelper.isLoggedIn(requireContext())
        findNavController().navigate(R.id.navigation_dashboard)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()

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