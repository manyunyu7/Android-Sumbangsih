package com.feylabs.sumbangsih.presentation._otp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.feylabs.sumbangsih.databinding.ActivityWriteOtpactivityBinding
import com.feylabs.sumbangsih.util.BaseActivity
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import org.koin.android.viewmodel.ext.android.viewModel

class WriteOTPActivity : BaseActivity() {
    val binding by lazy { ActivityWriteOtpactivityBinding.inflate(layoutInflater) }
    lateinit var mContext: Context

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
            hideLoading()
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            hideLoading()
            Toast.makeText(
                mContext,
                p0.message.toString(),
                Toast.LENGTH_LONG
            ).show()
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            hideLoading()
            verificationId = p0
            goToNextActivity(verificationId)
        }
    }


    private fun initObserver() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        hideActionBar()
        initObserver()

        binding.btnBack.setOnClickListener {
            finish()
            super.onBackPressed()
        }

        mContext = this

        binding.btnSendOtpCode.setOnClickListener {
            if (binding.inputMobile.text.trim().isEmpty()) {
                Toast.makeText(this, "Isi Terlebih Dahulu", Toast.LENGTH_LONG).show()
            } else {
                showLoading()
                val options = PhoneAuthOptions.newBuilder()
                    .setPhoneNumber("+62${binding.inputMobile.text.trim()}")       // Phone number to verify
                    .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(this)                 // Activity (for callback binding)
                    .setCallbacks(otpCallback)          // OnVerificationStateChangedCallbacks
                    .build()

                PhoneAuthProvider.verifyPhoneNumber(options)
            }
        }
    }

    fun goToNextActivity(verificationId: String) {
        intent_next = Intent(this, ReceiveOTPActivity::class.java)
        intent_next?.putExtra("verificationId", verificationId)
        intent_next?.putExtra("mobile", "62" + binding.inputMobile.text.toString())
        RazPreferenceHelper.savePhoneNumber(this, "62" + binding.inputMobile.text.toString())
        startActivity(intent_next)
    }
}