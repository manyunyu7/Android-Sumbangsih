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
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import com.feylabs.sumbangsih.util.sharedpref.RazPreferences
import com.feylabs.sumbangsih.util.sharedpref.RazPreferencesConst


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

        val number = RazPreferences(requireContext()).getPrefString(RazPreferencesConst.USER_NUMBER)
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

            }
            binding.apply {
                btnGoToVerif.text = "Login"
                this.tvDescPin.text =
                    "Masukkan pin yang telah Anda atur ketika melakukan pendaftaran"
                this.tvTitlePin.text = "Masukkan Pin Anda"
            }

            showToast(RazPreferences(requireContext()).getPrefString(RazPreferencesConst.USER_NUMBER))
        }

        if (isLoggedIn) {
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