package com.feylabs.sumbangsih.presentation

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.ActivityCommonControllerBinding
import com.feylabs.sumbangsih.util.BaseActivity

class CommonControllerActivity : BaseActivity() {

    private lateinit var binding: ActivityCommonControllerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        navController.navigate(R.id.createPinFragment)


        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (getCurrentNav()) {
                R.id.createPinFragment, R.id.verifPinFragment -> {
                    binding.navView.visibility = View.GONE
                    hideActionBar()
                }
                else -> {
                    binding.navView.visibility = View.VISIBLE
                    showActionBar()
                }
            }

        }

    }

    fun showNavView(){
        binding.navView.visibility = View.VISIBLE
    }

    fun hideNavView(){
        binding.navView.visibility = View.GONE
    }

    private fun getCurrentNav(): Int? {
        val navController = findNavController(R.id.nav_host_fragment_activity_common_controller)
        return navController.currentDestination?.id
    }
}