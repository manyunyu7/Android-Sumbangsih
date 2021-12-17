package com.feylabs.sumbangsih.presentation.pengajuan.without_sku

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.ProfileMainReq
import com.feylabs.sumbangsih.databinding.BltWithoutSkuStep1FragmentBinding
import com.feylabs.sumbangsih.databinding.BltWithoutSkuStep2FragmentBinding
import com.feylabs.sumbangsih.databinding.KtpVerifStep3FragmentBinding
import com.feylabs.sumbangsih.presentation.ktp_verif.model_json.KTPVerifReq
import com.feylabs.sumbangsih.presentation.ktp_verif.model_json.VerifNIKHelper
import com.feylabs.sumbangsih.presentation.ui.home.HomeViewModel
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.ImageView
import com.feylabs.sumbangsih.util.ImageView.convertViewToBase64
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import com.feylabs.sumbangsih.util.sharedpref.RazPreferences
import com.google.gson.Gson
import com.yalantis.ucrop.UCrop
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File

class BltWithoutSkuStep2Fragment : BaseFragment() {

    var _binding: BltWithoutSkuStep2FragmentBinding? = null
    val binding get() = _binding as BltWithoutSkuStep2FragmentBinding

    private val viewModel: HomeViewModel by viewModel()

    private var objVerif: KTPVerifReq? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BltWithoutSkuStep2FragmentBinding.inflate(inflater)
        return binding.root
    }

    private fun initObserver() {
        viewModel.profileLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ManyunyuRes.Default -> {
                    setProfileLoading(false)
                }
                is ManyunyuRes.Empty -> {
                    setProfileLoading(false)
                }
                is ManyunyuRes.Error -> {
                    setProfileLoading(false)
                }
                is ManyunyuRes.Loading -> {
                    setProfileLoading(true)
                }
                is ManyunyuRes.Success -> {
                    val data = it.data
                    if (data != null) {
                        setupProfileCard(data)
                    }
                    setProfileLoading(false)
                }
            }
        })
    }

    private fun setupProfileCard(data: ProfileMainReq) {
        val mKtp = data.resData.ktp
        val user = data.resData.user

        binding.apply {
            if (mKtp != null) {
                inputName.setText(mKtp.name)
                inputAddress.setText(mKtp.alamat)
                inputJk.setText(
                    if (mKtp.jk == "0")
                        "Laki-Laki"
                    else
                        "Perempuan"
                )
                inputNik.setText(mKtp.nik)
                inputNoKk.setText(mKtp.noKk)
                inputTtl.setText(mKtp.birthPlace + ", " + mKtp.birthDate)
                inputContact.setText(user?.contact.toString())
            }
        }

    }

    private fun setProfileLoading(b: Boolean) {
        if (b) {
            binding.includeLoading.root.makeViewVisible()
        } else {
            binding.includeLoading.root.makeViewGone()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initData()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        val photoUri: Uri? = arguments?.getString("uri")?.toUri()

//        objVerif = VerifNIKHelper.getKTPVerifReq(requireContext())

        binding.btnAjukan.isEnabled = false
        binding.checkbox.setOnCheckedChangeListener { compoundButton, b ->
            binding.btnAjukan.isEnabled = compoundButton.isChecked
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initData() {
        viewModel.getProfile(RazPreferenceHelper.getUserId(requireContext()))
    }


}




