package com.feylabs.sumbangsih.presentation.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.ProfileFragmentBinding
import com.feylabs.sumbangsih.presentation.CommonControllerActivity
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.CommonHelper.logout
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper

class ProfileFragment : BaseFragment() {

    private var _binding: ProfileFragmentBinding? = null
    val binding get() = _binding as ProfileFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideActionBar()
        (getActivity() as CommonControllerActivity).hideCustomTopbar()


        binding.includeListMenu.apply {
            tvMenu4.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_profileFragment_to_nav_chatCsFragment)
            }
            btnLogout.setOnClickListener {
                logout()
            }
        }
    }


}