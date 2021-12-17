package com.feylabs.sumbangsih.presentation.pengajuan.with_sku

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.ProfileMainReq
import com.feylabs.sumbangsih.databinding.BltSkuStep3FragmentBinding
import com.feylabs.sumbangsih.presentation.ktp_verif.model_json.KTPVerifReq
import com.feylabs.sumbangsih.presentation.ui.home.HomeViewModel
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import org.koin.android.viewmodel.ext.android.viewModel

class BltSkuStep3Fragment : BaseFragment() {

    var _binding: BltSkuStep3FragmentBinding? = null
    val binding get() = _binding as  BltSkuStep3FragmentBinding

    private val viewModel: HomeViewModel by viewModel()

    private var objVerif: KTPVerifReq? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  BltSkuStep3FragmentBinding.inflate(inflater)
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




