package com.feylabs.sumbangsih.presentation.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.ProfileMainReq
import com.feylabs.sumbangsih.databinding.ProfileFragmentBinding
import com.feylabs.sumbangsih.presentation.CommonControllerActivity
import com.feylabs.sumbangsih.presentation.ui.home.HomeViewModel
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.CommonHelper.logout
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment() {

    private var _binding: ProfileFragmentBinding? = null
    val binding get() = _binding as ProfileFragmentBinding
    val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(inflater)
        return binding.root
    }

    private fun initData() {
        homeViewModel.getProfile(RazPreferenceHelper.getUserId(requireContext()))
    }

    private fun initObserver() {
        homeViewModel.profileLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ManyunyuRes.Default -> {
                }
                is ManyunyuRes.Empty -> {
                }
                is ManyunyuRes.Error -> {
                    setupProfileCard(isError = true)
                }
                is ManyunyuRes.Loading -> {
                }
                is ManyunyuRes.Success -> {
                    val data = it.data
                    if (data != null) {
                        setupProfileCard(data,false)
                    } else {
                        setupProfileCard(isError = true)
                    }
                }
            }
        })
    }

    private fun setupProfileCard(data: ProfileMainReq?=null,isError:Boolean=false) {
        val mKtp = data?.resData?.ktp
        val user = data?.resData?.user

        if (mKtp == null) {
            binding.includeListMenu.tvMenu1.setOnClickListener {
                showToast("NIK Anda Belum Diverifikasi, Silakan melakukan pengajuan NIK")
            }
            binding.tvMain.text = "Halo, +" + RazPreferenceHelper.getPhoneNumber(requireContext())
            binding.tvDesc.text = "Segera lakukan verifikasi NIK"
        } else {
            binding.tvMain.text = "Halo, " + mKtp.name
            binding.tvDesc.text = "NIK : " + mKtp.nik
            binding.includeListMenu.tvMenu1.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_profileFragment_to_dataDiriFragment)
            }
        }

        if (isError){
            binding.tvMain.text = "Halo, +" + RazPreferenceHelper.getPhoneNumber(requireContext())
            binding.tvDesc.text = "Segera lakukan verifikasi NIK"
            binding.includeListMenu.tvMenu1.setOnClickListener {
                showToast("NIK Anda Belum Diverifikasi, Silakan melakukan pengajuan NIK")
            }
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideActionBar()
        initUi()
        initData()
        initObserver()
        (getActivity() as CommonControllerActivity).hideCustomTopbar()


        binding.includeListMenu.apply {
            tvMenu2.setOnClickListener {
                findNavController().navigate(R.id.checkPinFragment, bundleOf("razky" to "1"))
            }

            tvMenu4.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_profileFragment_to_nav_chatCsFragment)
            }
            btnLogout.setOnClickListener {
                logout()
            }
        }
    }

    private fun initUi() {
    }


}