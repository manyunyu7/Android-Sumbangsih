package com.feylabs.sumbangsih.presentation.pengajuan.with_sku

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.BltSkuStep2FragmentBinding
import com.feylabs.sumbangsih.presentation.ktp_verif.model_json.KTPVerifReq
import com.feylabs.sumbangsih.presentation.ktp_verif.model_json.VerifNIKHelper
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.ImageView
import com.feylabs.sumbangsih.util.sharedpref.RazPreferences

class BltSkuStep2Fragment : BaseFragment() {

    var _binding: BltSkuStep2FragmentBinding? = null
    val binding get() = _binding as BltSkuStep2FragmentBinding

    private var objVerif: PengajuanSKU? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BltSkuStep2FragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        val photoUri: Uri? = arguments?.getString("uri")?.toUri()
        val latPhoto = arguments?.getString("lat")
        val longPhoto = arguments?.getString("long")

        objVerif = PengajuanSKUObjectHelper.getObject(requireContext())

        binding.apply {

            val name = RazPreferences(requireContext()).getPrefString("usaha_name") ?: ""

            if (name.isNotBlank()) {
                binding.inputUsahaName.setText(name)
            }

            if (photoUri != null) {
                hideInputTextForm(false)
                btnNext.isEnabled = true
                setupTvListener()
                ivPhoto.viewTreeObserver
                    .addOnGlobalLayoutListener(object :
                        ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            val width: Int = ivPhoto.getWidth()
                            val height: Int = ivPhoto.getHeight()
                            //you can add your code here on what you want to do to the height and width you can pass it as parameter or make width and height a global variable
                            ivPhoto.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            val bitmapImage = ImageView.convertViewToBase64(
                                binding.ivPhoto,
                                width = width,
                                height = height
                            )
                            objVerif?.photo_usaha = bitmapImage
                            if (latPhoto != null) {
                                objVerif?.lat_usaha = latPhoto
                            }
                            if (longPhoto != null) {
                                objVerif?.long_usaha = longPhoto
                            }

                        }
                    })

                binding.btnNext.setOnClickListener {
                    objVerif?.nama_usaha = binding.inputUsahaName.text.toString()
                    PengajuanSKUObjectHelper.savePref(requireContext(), objVerif)
                    findNavController().navigate(
                        R.id.action_nav_bltSkuStep2Fragment_to_bltSkuStep3Fragment,
                    )
                    RazPreferences(requireContext()).removeKey("usaha_name")
                    RazPreferences(requireContext()).removeKey("usaha_nib")
                }

                tvDesc.text = "Jika foto dirasa kurang pas, anda bisa foto ulang lagi lho!"
                ivPhoto.setImageURI(photoUri)
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
            val name = binding.inputUsahaName.text.toString() ?: ""

            RazPreferences(requireContext()).save("usaha_name", name)

            findNavController().navigate(
                R.id.nav_take_photo_fragment,
                bundleOf("type" to "bltsku_step2")
            )
        }

        binding.btnPhotoAgain.setOnClickListener {
            val name = binding.inputUsahaName.text.toString() ?: ""

            RazPreferences(requireContext()).save("usaha_name", name)

            findNavController().navigate(
                R.id.nav_take_photo_fragment,
                bundleOf("type" to "bltsku_step2")
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
                binding.btnNext.isEnabled = p0.toString().isNotEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun hideInputTextForm(b: Boolean) {
        binding.apply {
            if (b) {
                labelName.makeViewGone()
                containerName.makeViewGone()
            } else {
                labelName.makeViewVisible()
                containerName.makeViewVisible()
            }
        }

    }


}




