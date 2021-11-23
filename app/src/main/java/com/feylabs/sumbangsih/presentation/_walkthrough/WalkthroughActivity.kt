package com.feylabs.sumbangsih.presentation._walkthrough

import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.feylabs.sumbangsih.MainActivity
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.ActivityWalkthroughBinding
import com.feylabs.sumbangsih.presentation._otp.ReceiveOTPActivity
import com.feylabs.sumbangsih.presentation._otp.WriteOTPActivity
import com.feylabs.sumbangsih.util.BaseActivity

class WalkthroughActivity : BaseActivity() {

    val binding by lazy { ActivityWalkthroughBinding.inflate(layoutInflater) }
    var currentItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViewpager()

        binding.btnSkip.setOnClickListener {
            goToNextActivity()
        }

        binding.btnNext.setOnClickListener {
            if (currentItem < 2)
                binding.viewPager.currentItem = currentItem + 1
            else if (currentItem == 2)
                goToNextActivity()
        }
    }

    private fun goToNextActivity() {
        startActivity(Intent(this, WriteOTPActivity::class.java))
        finish()
    }

    private fun setupViewpager() {
        val tabAdapter = WalktroughAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = tabAdapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentItem = position

                val arrayTitle = arrayListOf(
                    getString(R.string.title_on_board_1),
                    getString(R.string.title_on_board_2),
                    getString(R.string.title_on_board_3)
                )
                val arrayDesc = arrayListOf(
                    getString(R.string.text_on_board_1),
                    getString(R.string.text_on_board_2),
                    getString(R.string.text_on_board_3)
                )

                binding.apply {
                    if (position <= 2) {
                        labelTitleJargon.text = arrayTitle[position]
                        labelDescJargon.text = arrayDesc[position]
                        labelDescJargon.textSize = 14.0f
                        btnSkip.text = getString(R.string.skip)
                        labelDescJargon.makeViewVisible()
                        labelTitleJargon.makeViewVisible()
                    }
                }


            }
        })
    }


}