package com.feylabs.sumbangsih.presentation._otp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.feylabs.sumbangsih.MainActivity
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.ActivityReceiveOtpactivityBinding
import com.feylabs.sumbangsih.presentation.CommonControllerActivity
import com.feylabs.sumbangsih.util.BaseActivity
import com.google.android.gms.common.internal.service.Common
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider

class ReceiveOTPActivity : BaseActivity() {

    lateinit var mContext: Context

    val binding by lazy { ActivityReceiveOtpactivityBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        hideActionBar()

        mContext = this

        val number = intent.getStringExtra("mobile")
        val verifId = intent.getStringExtra("verificationId")


        binding.btnVerif.setOnClickListener {
            binding.apply {
                val code = "${inputCode1.text}" +
                        "${inputCode2.text}" +
                        "${inputCode3.text}" +
                        "${inputCode4.text}" +
                        "${inputCode5.text}" +
                        "${inputCode6.text}"
                if (verifId != null) {
                    progressBar.visibility = View.VISIBLE
                    btnVerif.visibility = View.GONE

                    val phoneAuthCredential = PhoneAuthProvider.getCredential(
                        verifId, code
                    )

                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener {
                            progressBar.visibility = View.GONE
                            btnVerif.visibility = View.VISIBLE

                            if (it.isSuccessful) {
                                Toast.makeText(
                                    this@ReceiveOTPActivity,
                                    "Login Berhasil",
                                    Toast.LENGTH_LONG
                                ).show()
                                gotoNextActivity()
                            } else {
                                Toast.makeText(
                                    this@ReceiveOTPActivity,
                                    "Kode OTP Salah",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                }
            }
        }

        keyboardOTPListener()
    }

    private fun gotoNextActivity() {
        val intent = Intent(this@ReceiveOTPActivity,CommonControllerActivity::class.java)
        startActivity(intent)
    }


    private fun keyboardOTPListener() {
        binding.inputCode1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    binding.inputCode2.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        binding.inputCode2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    binding.inputCode3.requestFocus()
                } else {
                    binding.inputCode1.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        binding.inputCode3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    binding.inputCode4.requestFocus()
                } else {
                    binding.inputCode2.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        binding.inputCode4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    binding.inputCode5.requestFocus()
                } else {
                    binding.inputCode3.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        binding.inputCode5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    binding.inputCode6.requestFocus()
                } else {
                    binding.inputCode4.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        binding.inputCode6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isEmpty()) {
                    binding.inputCode5.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

    }
}
