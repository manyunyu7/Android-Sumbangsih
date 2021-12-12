package com.feylabs.sumbangsih.presentation._otp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.feylabs.sumbangsih.SharedViewModel
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.databinding.ActivityReceiveOtpactivityBinding
import com.feylabs.sumbangsih.presentation.CommonControllerActivity
import com.feylabs.sumbangsih.util.BaseActivity
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import com.feylabs.sumbangsih.util.sharedpref.RazPreferences
import com.feylabs.sumbangsih.util.sharedpref.RazPreferencesConst
import com.feylabs.sumbangsih.util.sharedpref.RazPreferencesConst.HAS_NUMBER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import org.koin.android.viewmodel.ext.android.viewModel

class ReceiveOTPActivity : BaseActivity() {

    private val vm: ReceiveOTPViewModel by viewModel()

    lateinit var mContext: Context

    val binding by lazy { ActivityReceiveOtpactivityBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        hideActionBar()

        mContext = this

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
                        verifId.toString(), code
                    )

                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener {
                            progressBar.visibility = View.GONE
                            btnVerif.visibility = View.VISIBLE

                            if (it.isSuccessful) {
                                showToast("Login Berhasil")
                                gotoNextActivity()
                            } else {
                                showToast("Kode OTP Salah")
                            }
                        }
                } else {
                    showToast("Verif ID is Null")
                }
            }
        }

        keyboardOTPListener()
    }

    private fun gotoNextActivity() {

        val hasNumber = RazPreferences(this).getPrefBool(HAS_NUMBER)
        val number = intent.getStringExtra("mobile")

        if (hasNumber) {
            RazPreferences(this).save(HAS_NUMBER, hasNumber)
            RazPreferenceHelper.savePhoneNumber(this, number.toString())
        }

        val intent = Intent(this@ReceiveOTPActivity, CommonControllerActivity::class.java)
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
