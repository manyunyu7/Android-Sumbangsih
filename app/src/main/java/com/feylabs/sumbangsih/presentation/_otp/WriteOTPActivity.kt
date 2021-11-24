package com.feylabs.sumbangsih.presentation._otp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.ActivityWriteOtpactivityBinding
import com.feylabs.sumbangsih.util.BaseActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

class WriteOTPActivity : BaseActivity() {
    val binding by lazy { ActivityWriteOtpactivityBinding.inflate(layoutInflater) }
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        hideActionBar()

        mContext = this

        binding.btnSendOtpCode.setOnClickListener {
            if (binding.inputMobile.text.trim().isEmpty()) {
                Toast.makeText(this, "Please fill text", Toast.LENGTH_LONG).show()
            } else {

                binding.progressBar.visibility = View.VISIBLE
                binding.btnSendOtpCode.visibility = View.GONE


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

                        val intent = Intent(mContext, ReceiveOTPActivity::class.java)
                        intent.putExtra("mobile", binding.inputMobile.text.toString())
                        intent.putExtra("verificationId", p0)
                        startActivity(intent)
                        Toast.makeText(
                            mContext,
                            "OTP Terkirim ke +62${binding.inputMobile.text}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }

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
}