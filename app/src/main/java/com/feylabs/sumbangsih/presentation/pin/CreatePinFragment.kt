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
import com.feylabs.sumbangsih.presentation.CommonControllerActivity
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import com.feylabs.sumbangsih.util.sharedpref.RazPreferences
import com.feylabs.sumbangsih.util.sharedpref.RazPreferencesConst
import org.koin.android.viewmodel.ext.android.viewModel


class CreatePinFragment : BaseFragment() {

    var _binding: FragmentCreatePinBinding? = null
    val binding get() = _binding as FragmentCreatePinBinding
    val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun initObserver() {
        viewModel.loginNumberLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ManyunyuRes.Success -> {
                    proceedLogin(it.data)
                }
                is ManyunyuRes.Default -> {
                }
                is ManyunyuRes.Empty -> {
                    showToast("Login Gagal")
                }
                is ManyunyuRes.Error -> {
                    showToast(it.message)
                }
                is ManyunyuRes.Loading -> {
                    showToast("Loading")
                }
            }
        })
    }

    private fun proceedLogin(data: LoginWithNumberRes?) {
        showToast("Login Berhasil")
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
        (getActivity() as CommonControllerActivity).hideCustomTopbar()
        initObserver()

        val number = RazPreferenceHelper.getPhoneNumber(requireContext())
        val hasNumber =
            RazPreferences(requireContext()).getPrefBool(RazPreferencesConst.HAS_NUMBER) ?: false

        val isLoggedIn = RazPreferenceHelper.isLoggedIn(requireContext()) ?: false

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

        // if user has number that registered to server
        if (hasNumber) {
            binding.btnGoToVerif.setOnClickListener {
                viewModel.loginNumber(number, binding.passCodeView.passCodeText)
            }
            binding.apply {
                btnGoToVerif.text = "Login"
                this.tvDescPin.text =
                    "Masukkan pin yang telah Anda atur ketika melakukan pendaftaran"
                this.tvTitlePin.text = "Masukkan Pin Anda"
            }
        }

        if (isLoggedIn) {
            binding.passCodeView.setOnTextChangeListener {
                if (it.length == 6) {
                    viewModel.loginNumber(number, binding.passCodeView.passCodeText)
                }
            }
            binding.btnGoToVerif.setOnClickListener {
                viewModel.loginNumber(number, binding.passCodeView.passCodeText)
            }
            val loggedInNumber = RazPreferenceHelper.getPhoneNumber(requireContext())
            binding.apply {
                tvChangeNumber.makeViewVisible()
                tvForgotPin.makeViewVisible()
                btnGoToVerif.text = "Login"
                this.tvDescPin.text =
                    "Masukkan pin yang telah Anda atur ketika melakukan pendaftaran (${loggedInNumber})"
                this.tvTitlePin.text = "Masukkan Pin Anda"
            }
        }

    }

}