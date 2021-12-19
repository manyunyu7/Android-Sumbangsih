package com.feylabs.sumbangsih.presentation.pengajuan.without_sku

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.BltWithoutSkuStep1FragmentBinding
import com.feylabs.sumbangsih.databinding.KtpVerifStep3FragmentBinding
import com.feylabs.sumbangsih.presentation.ktp_verif.model_json.KTPVerifReq
import com.feylabs.sumbangsih.presentation.ktp_verif.model_json.VerifNIKHelper
import com.feylabs.sumbangsih.presentation.pengajuan.with_sku.PengajuanSKU
import com.feylabs.sumbangsih.presentation.pengajuan.with_sku.PengajuanSKUObjectHelper
import com.feylabs.sumbangsih.presentation.pengajuan.with_sku.PengajuanWithoutSKU
import com.feylabs.sumbangsih.presentation.pengajuan.with_sku.PengajuanWithoutSKUObjectHelper
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

class BltWithoutSkuStep1Fragment : BaseFragment() {

    var _binding: BltWithoutSkuStep1FragmentBinding? = null
    val binding get() = _binding as BltWithoutSkuStep1FragmentBinding

    private var objWithoutSKU: PengajuanWithoutSKU? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BltWithoutSkuStep1FragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        val photoUri: Uri? = arguments?.getString("uri")?.toUri()

        val idEvent = RazPreferences(requireContext()).getPrefString("event_id") ?: ""

        objWithoutSKU = PengajuanWithoutSKU(
            event_id = idEvent,
            user_id = RazPreferenceHelper.getUserId(requireContext())
        )

        binding.apply {

            val nib = RazPreferences(requireContext()).getPrefString("usaha_nib") ?: ""
            val name = RazPreferences(requireContext()).getPrefString("usaha_name") ?: ""

            if (nib.isNotBlank()) {
                binding.inputUsahaNib.setText(nib)
            }

            if (name.isNotBlank()) {
                binding.inputUsahaName.setText(name)
            }

            if (photoUri != null) {

                val latPhoto = arguments?.getString("lat")
                val longPhoto = arguments?.getString("long")

                hideInputTextForm(false)
                btnNext.isEnabled = true
                setupTvListener()
                imgKtp.viewTreeObserver
                    .addOnGlobalLayoutListener(object :
                        ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            val width: Int = imgKtp.width
                            val height: Int = imgKtp.height
                            //you can add your code here on what you want to do to the height and width you can pass it as parameter or make width and height a global variable
                            imgKtp.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            val bitmapImage = ImageView.convertViewToBase64(
                                binding.imgKtp,
                                width = width,
                                height = height
                            )
                            objWithoutSKU?.photo_usaha = bitmapImage!!
                            if (latPhoto != null) {
                                objWithoutSKU?.lat_usaha = latPhoto
                            }
                            if (longPhoto != null) {
                                objWithoutSKU?.long_usaha = longPhoto
                            }
                        }
                    })

                binding.btnNext.setOnClickListener {
                    objWithoutSKU?.nama_usaha = binding.inputUsahaName.text.toString()
                    objWithoutSKU?.nib = binding.inputUsahaNib.text.toString()
                    PengajuanWithoutSKUObjectHelper.savePref(requireContext(), objWithoutSKU)

                    findNavController().navigate(
                        R.id.action_nav_bltWithoutSkuStep1Fragment_to_bltWithoutSkuStep2Fragment,
                    )

                    RazPreferences(requireContext()).removeKey("usaha_name")
                    RazPreferences(requireContext()).removeKey("usaha_nib")
                }
                tvDesc.text = "Jika foto dirasa kurang pas, anda bisa foto ulang lagi lho!"
                imgKtp.setImageURI(photoUri)
                btnTakePhoto.makeViewGone()
                containerPhotoTaken.makeViewVisible()
            } else {
                hideInputTextForm(true)
                btnNext.isEnabled = false
                tvDesc.text =
                    "Pengajuan BLT UMKM secara online memerlukan foto Usaha yang dimiliki oleh Pemilik UMKM sebagai bukti yang akan di validasi oleh panitia"
                containerPhotoTaken.makeViewGone()
                btnTakePhoto.makeViewVisible()
            }
        }

        binding.btnTakePhoto.setOnClickListener {
            val nib = binding.inputUsahaNib.text.toString()
            val name = binding.inputUsahaName.text.toString()

            RazPreferences(requireContext()).save("usaha_name", name)
            RazPreferences(requireContext()).save("usaha_nib", nib)

            findNavController().navigate(
                R.id.nav_take_photo_fragment,
                bundleOf("type" to "bltnonsku_step1")
            )
        }

        binding.btnPhotoAgain.setOnClickListener {
            val nib = binding.inputUsahaNib.text.toString()
            val name = binding.inputUsahaName.text.toString()

            RazPreferences(requireContext()).save("usaha_name", name)
            RazPreferences(requireContext()).save("usaha_nib", nib)

            findNavController().navigate(
                R.id.nav_take_photo_fragment,
                bundleOf("type" to "bltnonsku_step1")
            )
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupTvListener() {
        binding.btnNext.isEnabled = false
        binding.inputUsahaName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    binding.btnNext.isEnabled = binding.inputUsahaNib.text.toString().isNotBlank()
                } else {
                    binding.btnNext.isEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.inputUsahaNib.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    binding.btnNext.isEnabled = binding.inputUsahaName.text.toString().isNotBlank()
                } else {
                    binding.btnNext.isEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun hideInputTextForm(b: Boolean) {
        binding.apply {
            if (b) {
                labelName.makeViewGone()
                labelNib.makeViewGone()
                containerNib.makeViewGone()
                containerName.makeViewGone()
            } else {
                labelName.makeViewVisible()
                labelNib.makeViewVisible()
                containerNib.makeViewVisible()
                containerName.makeViewVisible()
            }
        }
    }


}




