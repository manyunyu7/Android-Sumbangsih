package com.feylabs.sumbangsih.presentation._otp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.CheckNumberRes
import com.feylabs.sumbangsih.databinding.ActivityWriteOtpactivityBinding
import com.feylabs.sumbangsih.util.BaseActivity
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import com.feylabs.sumbangsih.util.sharedpref.RazPreferences
import com.feylabs.sumbangsih.util.sharedpref.RazPreferencesConst
import com.feylabs.sumbangsih.util.sharedpref.RazPreferencesConst.HAS_NUMBER
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class WriteOTPActivity : BaseActivity() {
    val binding by lazy { ActivityWriteOtpactivityBinding.inflate(layoutInflater) }
    lateinit var mContext: Context

    var isNumberExist = false
    private var verificationId = ""

    val viewModel: ReceiveOTPViewModel by viewModel()

    var intent_next: Intent? = null

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSendOtpCode.visibility = View.GONE
    }

    fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.btnSendOtpCode.visibility = View.VISIBLE
    }

    val otpCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            binding.progressBar.visibility = View.GONE
            binding.btnSendOtpCode.visibility = View.VISIBLE
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            binding.progressBar.visibility = View.GONE
            binding.btnSendOtpCode.visibility = View.VISIBLE
            Toast.makeText(
                mContext,
                p0.message.toString(),
                Toast.LENGTH_LONG
            ).show()
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            binding.progressBar.visibility = View.GONE
            binding.btnSendOtpCode.visibility = View.VISIBLE
            verificationId = p0

            intent_next?.putExtra("verificationId", verificationId)

            if (intent_next != null) {
                startActivity(intent_next)
            }
        }
    }


    private fun initObserver() {
        viewModel.checkNumberLiveData.observe(this, {

            when (it) {
                is ManyunyuRes.Success -> {
                    hideLoading()
                    goToNextActivity(it)
                }
                is ManyunyuRes.Error -> {
                    hideLoading()
                    Timber.d(it.message)
                    showToast(it.message.toString())
                }
                is ManyunyuRes.Loading -> {
                    showLoading()
                }
                is ManyunyuRes.Empty -> {
                    hideLoading()
                    goToNextActivity(it)
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        hideActionBar()

        initObserver()

        mContext = this


        binding.btnSendOtpCode.setOnClickListener {
            if (binding.inputMobile.text.trim().isEmpty()) {
                Toast.makeText(this, "Isi Terlebih Dahulu", Toast.LENGTH_LONG).show()
            } else {
                showLoading()
                // vmOps : checkNumber
                viewModel.checkNumber("62${binding.inputMobile.text.trim()}")
            }
        }
    }

    fun goToNextActivity(manyunyuRes: ManyunyuRes<CheckNumberRes?>) {
        val options = PhoneAuthOptions.newBuilder()
            .setPhoneNumber("+62${binding.inputMobile.text.trim()}")       // Phone number to verify
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(otpCallback)          // OnVerificationStateChangedCallbacks
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        intent_next = Intent(this, ReceiveOTPActivity::class.java)
        intent_next?.putExtra("mobile", "62" + binding.inputMobile.text.toString())

        RazPreferenceHelper.savePhoneNumber(this, "62" + binding.inputMobile.text.toString())

        when (manyunyuRes) {
            is ManyunyuRes.Empty -> {
//                showToast("Nomor Tidak Terdaftar")
                intent_next?.putExtra(HAS_NUMBER, "no")
                RazPreferences(this).save(HAS_NUMBER,false)
                showToast(RazPreferences(this).getPrefBool(HAS_NUMBER).toString())
            }
            is ManyunyuRes.Success -> {
                intent_next?.putExtra(HAS_NUMBER, "yes")
                RazPreferences(this).save(HAS_NUMBER,true)
                showToast(RazPreferences(this).getPrefBool(HAS_NUMBER).toString())
            }
            else->{
//                showToast("Nomor Belum Ditemukan Dalam Database")
                intent_next?.putExtra(HAS_NUMBER, "yes")
                RazPreferences(this).save(HAS_NUMBER,false)
                showToast(RazPreferences(this).getPrefBool(HAS_NUMBER).toString())
            }
        }
    }
}