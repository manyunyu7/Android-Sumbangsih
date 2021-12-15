package com.feylabs.sumbangsih.presentation.ktp_verif

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.databinding.KtpVerifStep2FragmentBinding
import com.feylabs.sumbangsih.presentation.ktp_verif.model_json.KTPVerifReq
import com.feylabs.sumbangsih.presentation.ktp_verif.model_json.VerifNIKHelper
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.sharedpref.RazPreferences
import org.koin.android.viewmodel.ext.android.viewModel

class KTPVerifStep2Fragment : BaseFragment() {

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

    private fun initData() {
        val ktp = arguments?.getString("nik") ?: ""
        viewModel.findKTPByNIK(ktp)
    }

    private fun initUi() {
        binding.srl.setOnRefreshListener {
            val ktp = arguments?.getString("nik") ?: ""
            viewModel.findKTPByNIK(ktp)
        }
        binding.tvNik.text = arguments?.getString("nik")
    }

    @SuppressLint("SetTextI18n")
    private fun initObserver() {
        viewModel.findKTPVm.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ManyunyuRes.Default -> {
                    binding.btnPositive.isEnabled = false
                    makeSrlLoading(false)
                    binding.includeLoading.root.makeViewGone()
                }
                is ManyunyuRes.Empty -> {
                    binding.btnPositive.isEnabled = false
                    makeSrlLoading(false)
                    binding.includeLoading.root.makeViewGone()
                }
                is ManyunyuRes.Error -> {
                    findNavController().popBackStack()
                    showToast("NIK Tidak Ditemukan")
                    binding.btnPositive.isEnabled = false
                    makeSrlLoading(false)
                    binding.includeLoading.root.makeViewGone()
                }
                is ManyunyuRes.Loading -> {
                    binding.btnPositive.isEnabled = false
                    makeSrlLoading(true)
                    binding.includeLoading.root.makeViewVisible()
                }
                is ManyunyuRes.Success -> {
                    binding.btnPositive.isEnabled = true
                    val data = it.data?.resData
                    binding.includeLoading.root.makeViewGone()
                    makeSrlLoading(false)
                    binding.tvName.text = data?.name.toString()
                    binding.tvBirthInfo.text = data?.birthPlace + ", " + data?.birthDate
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initObserver()
        initUi()

        binding.btnNegative.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnPositive.setOnClickListener {
            val objVerif = KTPVerifReq(
                nik = binding.tvNik.text.toString(),
                "", ""
            )
            VerifNIKHelper.savePref(
                requireContext(), KTPVerifReq(
                    nik = binding.tvNik.text.toString(),
                    "", ""
                )
            )
            findNavController().navigate(
                R.id.action_KTPVerifStep2Fragment_to_KTPVerifStep3Fragment,
            )
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}