package com.feylabs.sumbangsih.presentation._astart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.feylabs.sumbangsih.databinding.ActivitySplashScreenBinding
import com.feylabs.sumbangsih.presentation._walkthrough.WalkthroughActivity
import kotlinx.coroutines.*

class SplashScreenActivity : AppCompatActivity() {
    val binding by lazy { ActivitySplashScreenBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        GlobalScope.launch {
            delay(3000)
            withContext(Dispatchers.Main) {
                finish()
                startActivity(Intent(this@SplashScreenActivity, WalkthroughActivity::class.java))
            }
        }

    }
}