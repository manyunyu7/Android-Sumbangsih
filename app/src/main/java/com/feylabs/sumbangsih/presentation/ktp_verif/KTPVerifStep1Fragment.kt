package com.feylabs.sumbangsih.presentation.ktp_verif

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.KtpVerifStep1FragmentBinding
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.DialogUtils
import org.koin.android.viewmodel.ext.android.viewModel

class KTPVerifStep1Fragment : BaseFragment() {

    val viewModel: KTPVerifViewModel by viewModel()

    var _binding: KtpVerifStep1FragmentBinding? = null
    val binding get() = _binding as KtpVerifStep1FragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = KtpVerifStep1FragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNext.isEnabled = false

        binding.inputMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val length = p0?.length
                if (length == 16) {
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = "Perhatian",
                        message = "Panjang Maksimum NIK adalah berjumlah 16 Karakter",
                        positiveAction = Pair(getString(R.string.dialog_ok), { }),
                        negativeAction = Pair(getString(R.string.dialog_cancel), { }),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                val length = p0.length
                binding.btnNext.isEnabled = length == 16
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnNext.setOnClickListener {
            val nik = binding.inputMobile.text.toString()
            findNavController().navigate(
                R.id.action_KTPVerifStep1Fragment_to_KTPVerifStep2Fragment,
                bundleOf("nik" to nik)
            )
        }
    }


}