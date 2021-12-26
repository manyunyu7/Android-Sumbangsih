package com.feylabs.sumbangsih.presentation.data_diri

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.databinding.FragmentKomplainBinding
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.DialogUtils
import com.feylabs.sumbangsih.util.ImageView
import com.feylabs.sumbangsih.util.ObjectHelperCommon.serializeToMap
import com.feylabs.sumbangsih.util.UiUtils.addCurrencyTextWatcher
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import com.yalantis.ucrop.UCrop
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.util.Base64
import com.feylabs.sumbangsih.data.source.remote.response.ProfileMainReq
import com.feylabs.sumbangsih.databinding.FragmentDataDiriBinding
import com.feylabs.sumbangsih.presentation.komplain.KomplainRequestBody
import com.feylabs.sumbangsih.util.ImageView.convertImageToStringForServer
import com.feylabs.sumbangsih.util.ImageView.getBitmapFromView
import com.feylabs.sumbangsih.util.ImageView.loadImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


class DataDiriFragment : BaseFragment() {

    private var _binding: FragmentDataDiriBinding? = null
    val binding get() = _binding as FragmentDataDiriBinding

    private val viewModel: DataDiriViewModel by viewModel()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataDiriBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initData()
        initObserver()
    }

    private fun initData() {
        viewModel.getProfile(RazPreferenceHelper.getUserId(requireContext()))
    }

    private fun initObserver() {
        viewModel.profileLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ManyunyuRes.Default -> {
                    makeSrlLoading(false)
                    showFullscreenLoading(false)
                }
                is ManyunyuRes.Empty -> {
                    makeSrlLoading(false)
                    showFullscreenLoading(false)
                }
                is ManyunyuRes.Error -> {
                    makeSrlLoading(false)
                    showToast(it.message.toString())
                    showFullscreenLoading(false)
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = "Error",
                        message = "Terjadi Kesalahan : ${it.message}",
                        positiveAction = Pair(getString(R.string.dialog_ok), {}),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
                is ManyunyuRes.Loading -> {
                    makeSrlLoading(true)
                    showFullscreenLoading(true)
                }
                is ManyunyuRes.Success -> {
                    makeSrlLoading(false)
                    if (it.data != null) {
                        setupProfileCard(it.data)
                    }
                    showFullscreenLoading(false)
                }
            }
        })

    }

    @SuppressLint("SetTextI18n")
    private fun setupProfileCard(data: ProfileMainReq) {
        val mKtp = data.resData.ktp
        val user = data.resData.user

        binding.apply {
            if (mKtp != null) {
                tvAddress.text = mKtp.alamat
                tvName.text = mKtp.name
                tvGender.text = if (mKtp.jk == "0") "Laki-Laki" else "Perempuan"
                tvNik.text = mKtp.nik
                tvBirthInfo.text = mKtp.birthPlace + "," + " ${mKtp.birthDate}"
                imgKtp.loadImage(mKtp.photoKtpFullPath)
            }
        }

    }


    private fun initUi() {

        binding.btnLapor.setOnClickListener {
            findNavController().navigate(R.id.action_dataDiriFragment_to_reportMistakeFragment)
        }

        binding.srl.setOnRefreshListener {
            viewModel.getProfile(RazPreferenceHelper.getUserId(requireContext()))
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }


}