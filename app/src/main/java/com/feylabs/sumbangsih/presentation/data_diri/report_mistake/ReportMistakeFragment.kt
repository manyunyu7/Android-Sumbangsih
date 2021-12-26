package com.feylabs.sumbangsih.presentation.data_diri.report_mistake

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.DialogUtils
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import org.koin.android.viewmodel.ext.android.viewModel
import com.feylabs.sumbangsih.data.source.remote.response.ProfileMainReq
import com.feylabs.sumbangsih.databinding.FragmentReportMistakeBinding
import com.feylabs.sumbangsih.presentation.data_diri.DataDiriViewModel
import com.feylabs.sumbangsih.util.ImageView.loadImage
import java.util.*
import android.app.DatePickerDialog


class ReportMistakeFragment : BaseFragment() {

    private var _binding: FragmentReportMistakeBinding? = null
    val binding get() = _binding as FragmentReportMistakeBinding


    private val viewModel: DataDiriViewModel by viewModel()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportMistakeBinding.inflate(inflater)
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

        viewModel.uploadPengajuanVm.observe(viewLifecycleOwner, {
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
                    showFullscreenLoading(false)
                    showSuccess()
                }
            }
        })

    }

    private fun showSuccess() {
        binding.includeSuccess.apply {
            root.makeViewVisible()
            tvTitle.text = "Kesalahan Data Telah Diajukan"
            tvDesc.text =
                "Mohon maaf atas ketidaknyamanannya. Data anda sedang diproses, Mohon tunggu informasi selanjutnya"
            btnAction.setOnClickListener {
                findNavController().navigate(R.id.navigation_home)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupProfileCard(data: ProfileMainReq) {
        val mKtp = data.resData.ktp
        val user = data.resData.user

        binding.apply {
            if (mKtp != null) {
                inputAddress.setText(mKtp.alamat)
                inputName.setText(mKtp.name)

                if (mKtp.jk == "1") {
                    binding.spinnerGender.setSelection(0)
                } else {
                    binding.spinnerGender.setSelection(1)
                }

                inputBirthDate.setText(mKtp.birthDate)
                inputNik.setText(mKtp.nik)
                inputNoKk.setText(mKtp.noKk)
                inputBirthPlace.setText(mKtp.birthPlace)
                imgKtp.loadImage(mKtp.photoKtpFullPath)
                inputContact.setText(RazPreferenceHelper.getPhoneNumber(requireContext()))
            }
        }

    }


    private fun initUi() {

        val newCalendar = Calendar.getInstance()
        val picker = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth
                binding.inputBirthDate.setText("$year-$monthOfYear-$dayOfMonth")
            },
            newCalendar[Calendar.YEAR],
            newCalendar[Calendar.MONTH],
            newCalendar[Calendar.DAY_OF_MONTH]
        )

        binding.inputBirthDate.setOnClickListener {
            picker.show()
        }

        binding.btnAjukan.isEnabled = false
        binding.checkbox.setOnCheckedChangeListener { compoundButton, b ->
            binding.btnAjukan.isEnabled = compoundButton.isChecked
        }

        binding.btnAjukan.setOnClickListener {
            submit()
        }


        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun submit() {
        val map = mutableMapOf<String, String>()

        var gender = "0"

        if (binding.spinnerGender.selectedItemPosition == 0) {
            gender = "1"
        } else {
            gender = "2"
        }

        map.apply {
            binding.let {
                put("user_id", RazPreferenceHelper.getUserId(requireContext()))
                put("name", it.inputName.text.toString())
                put("birth_place", it.inputBirthPlace.text.toString())
                put("birth_date", it.inputBirthDate.text.toString())
                put("nik", it.inputNik.text.toString())
                put("no_kk", it.inputNoKk.text.toString())
                put("contact", it.inputContact.text.toString())
                put("alamat", it.inputAddress.text.toString())
                put("jk", gender)
            }
        }
        viewModel.upload(map)
    }


}