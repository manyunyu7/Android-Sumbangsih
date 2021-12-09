package com.feylabs.sumbangsih.presentation

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.SharedViewModel
import com.feylabs.sumbangsih.databinding.ActivityCommonControllerBinding
import com.feylabs.sumbangsih.util.AnimUtil
import com.feylabs.sumbangsih.util.BaseActivity
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import org.koin.android.viewmodel.ext.android.viewModel


class CommonControllerActivity : BaseActivity() {

    private lateinit var binding: ActivityCommonControllerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatusBar()

        binding = ActivityCommonControllerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_common_controller)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.navigate(R.id.navigation_createPinFragment)

        val loggedInPhoneNumber = RazPreferenceHelper.getPhoneNumber(this)
        if (RazPreferenceHelper.isLoggedIn(this) && loggedInPhoneNumber.isNotEmpty()) {
            navController.navigate(R.id.navigation_home)
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (getCurrentNav()) {
                R.id.navigation_createPinFragment, R.id.navigation_verifPinFragment,
                R.id.navigation_detailTutorialFragment, R.id.navigation_videoPlayerFragment
                -> {
                    hideNavView()
                    hideActionBar()
                }
                else -> {
                    showNavView()
                    showActionBar()
                }
            }

        }

    }

    private fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
    }

    override fun onBackPressed() {
        when (getCurrentNav()) {
            R.id.navigation_createPinFragment -> {

            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    fun showNavView() {
        binding.navView.visibility = View.VISIBLE
//        AnimUtil.animateFromResource(binding.navView,R.anim.anim_slide_in_top)
    }

    fun hideNavView() {
        AnimUtil.animateFromResource(binding.navView,R.anim.anim_slide_out_bottom)
        binding.navView.visibility = View.GONE
    }

    private fun getCurrentNav(): Int? {
        val navController = findNavController(R.id.nav_host_fragment_activity_common_controller)
        return navController.currentDestination?.id
    }
}