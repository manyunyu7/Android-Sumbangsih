package com.feylabs.sumbangsih.presentation.pengajuan

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.PengajuanBltFragmentBinding
import com.feylabs.sumbangsih.util.BaseFragment

class PengajuanBLTFragment : BaseFragment() {


    private var _binding: PengajuanBltFragmentBinding? = null
    private val binding get() = _binding as PengajuanBltFragmentBinding

    private lateinit var viewModel: PengajuanBLTViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PengajuanBltFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.indicatorBltActive.makeViewVisible()
        binding.indicatorBltHistory.makeViewGone()

        binding.tvActive.setOnClickListener {
            binding.indicatorBltHistory.makeViewGone()
            binding.indicatorBltActive.makeViewVisible()

            binding.includeActive.root.makeViewVisible()
            binding.includeHistory.root.makeViewGone()
        }

        binding.tvHistory.setOnClickListener {
            binding.indicatorBltHistory.makeViewVisible()
            binding.indicatorBltActive.makeViewGone()

            binding.includeActive.root.makeViewGone()
            binding.includeHistory.root.makeViewVisible()
        }

        binding.includeActive.btnAction.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_pengajuanBLTFragment_to_pengajuanBLTStepInitialFragment)
        }
    }

}