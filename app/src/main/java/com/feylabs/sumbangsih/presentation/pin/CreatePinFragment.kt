package com.feylabs.sumbangsih.presentation.pin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.LoginWithNumberRes
import com.feylabs.sumbangsih.databinding.FragmentCreatePinBinding
import com.feylabs.sumbangsih.presentation._otp.ReceiveOTPViewModel
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.CommonHelper
import com.feylabs.sumbangsih.util.DialogUtils
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import org.koin.android.viewmodel.ext.android.viewModel


class CreatePinFragment : BaseFragment() {

    var _binding: FragmentCreatePinBinding? = null
    val binding get() = _binding as FragmentCreatePinBinding
    val viewModel: AuthViewModel by viewModel()
    val receiveOTPViewModel: ReceiveOTPViewModel by viewModel()

    var number = ""

    private fun initData() {
        number = RazPreferenceHelper.getPhoneNumber(requireContext())
        receiveOTPViewModel.checkNumber(number)
    }

    private fun initObserver() {
        receiveOTPViewModel.checkNumberLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ManyunyuRes.Success -> {
                    setLoadingNumber(false)
                    binding.includeShimmerPage.root.makeViewGone()
                    if (it.data != null) {
                        val apiCode = it.data.apiCode
                        if (apiCode == 1) {
                            setViewHasNumber()
                        }else{
                            setViewDontHaveNumber()
                        }
                    }
                }
                is ManyunyuRes.Error -> {
                    setLoadingNumber(false)
                    binding.includeShimmerPage.root.makeViewGone()
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = "Terjadi Kesalahan",
                        message = "Koneksi Internet Tidak Ditemukan, Perangkat Tidak Dapat Menghubungi Server",
                        positiveAction = Pair("Coba Lagi", {
                            number = RazPreferenceHelper.getPhoneNumber(requireContext())
                            receiveOTPViewModel.checkNumber(number)
                        }),
                        negativeAction = Pair(
                            "Tutup Aplikasi",
                            { CommonHelper.logout(requireContext()) }),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
                is ManyunyuRes.Loading -> {
                    setLoadingNumber(true)
                }
                is ManyunyuRes.Empty -> {
                    setViewDontHaveNumber()
                    setLoadingNumber(false)
                }
                is ManyunyuRes.Default -> {
                    setLoadingNumber(false)
                }
            }
        })

        viewModel.loginNumberLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ManyunyuRes.Success -> {
                    binding.includeLoading.root.makeViewGone()
                    proceedLogin(it.data)
                }
                is ManyunyuRes.Default -> {
                }
                is ManyunyuRes.Empty -> {
                    binding.includeLoading.root.makeViewGone()
                }
                is ManyunyuRes.Error -> {
                    binding.includeLoading.root.makeViewGone()
                    showToast(it.message)
                }
                is ManyunyuRes.Loading -> {
                    binding.includeLoading.root.makeViewVisible()
                }
            }
        })
    }

    private fun setViewDontHaveNumber() {
        val loggedInNumber =
            RazPreferenceHelper.getPhoneNumber(requireContext())
        binding.apply {
//            tvChangeNumber.makeViewVisible()
//            tvForgotPin.makeViewVisible()
            btnGoToVerif.text = "Selanjutnya"
            this.tvDescPin.text =
                "Masukkan Pin Yang Akan Anda Gunakan Untuk Akun Dengan Nomor (${loggedInNumber})"
            this.tvTitlePin.text = "Masukkan Pin Anda"
        }
        binding.btnGoToVerif.setOnClickListener {
            val text = binding.passCodeView.passCodeText.toString().length
            if (text < 6) {
                "Lengkapi Inputan".showLongToast()
            } else {
                findNavController().navigate(
                    R.id.navigation_verifPinFragment, bundleOf(
                        "firstPin" to binding.passCodeView.passCodeText
                    )
                )
            }
        }

    }

    private fun setLoadingNumber(b: Boolean) {
        if (b) {
            binding.defaultLayout.makeViewGone()
            binding.includeShimmerPage.root.makeViewVisible()
        } else {
            binding.defaultLayout.makeViewVisible()
            binding.includeShimmerPage.root.makeViewGone()
        }
    }

    private fun setViewHasNumber() {
        val number = RazPreferenceHelper.getPhoneNumber(requireContext())
        binding.btnBack.makeViewGone()

        binding.btnGoToVerif.setOnClickListener {
            viewModel.loginNumber(number, binding.passCodeView.passCodeText)
        }
        val loggedInNumber =
            RazPreferenceHelper.getPhoneNumber(requireContext())
        binding.apply {
//            tvChangeNumber.makeViewVisible()
//            tvForgotPin.makeViewVisible()
            btnGoToVerif.text = "Login"
            this.tvDescPin.text =
                "Masukkan pin yang telah Anda atur ketika melakukan pendaftaran (${loggedInNumber})"
            this.tvTitlePin.text = "Masukkan Pin Anda"
        }
    }

    private fun proceedLogin(data: LoginWithNumberRes?) {
        showToast("Login Berhasil")
        viewModel.fireLogin()
        RazPreferenceHelper.saveUserId(requireContext(), data?.user?.id.toString())
        RazPreferenceHelper.setLoggedIn(requireContext())
        findNavController().navigate(R.id.navigation_home)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCreatePinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initObserver()

        binding.btnGoToVerif.setOnClickListener {
            val text = binding.passCodeView.passCodeText.toString().length
            if (text < 6) {
                "Lengkapi Inputan".showLongToast()
            } else {
                findNavController().navigate(
                    R.id.navigation_verifPinFragment, bundleOf(
                        "firstPin" to binding.passCodeView.passCodeText
                    )
                )
            }
        }

    }

}