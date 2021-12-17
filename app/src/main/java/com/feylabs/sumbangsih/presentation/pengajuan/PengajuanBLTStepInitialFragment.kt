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
import com.feylabs.sumbangsih.databinding.PengajuanBltStepInitialFragmentBinding
import com.feylabs.sumbangsih.util.BaseFragment

class PengajuanBLTStepInitialFragment : BaseFragment() {

    private var _binding: PengajuanBltStepInitialFragmentBinding? = null
    private val binding get() = _binding as PengajuanBltStepInitialFragmentBinding

    private lateinit var viewModel: PengajuanBLTViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PengajuanBltStepInitialFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSkip.setOnClickListener {
            findNavController().navigate(R.id.action_nav_pengajuanBLTStepInitialFragment_to_bltWithoutSkuStep1Fragment)
        }

    }


}