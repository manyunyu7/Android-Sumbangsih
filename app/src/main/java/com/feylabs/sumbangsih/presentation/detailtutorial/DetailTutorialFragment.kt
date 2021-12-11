package com.feylabs.sumbangsih.presentation.detailtutorial

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.DetailTutorialFragmentBinding
import com.feylabs.sumbangsih.util.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

import com.google.android.material.bottomnavigation.BottomNavigationView


class DetailTutorialFragment : BaseFragment() {

    private var _binding: DetailTutorialFragmentBinding? = null
    val binding get() = _binding as DetailTutorialFragmentBinding

    val viewModel: DetailTutorialViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailTutorialFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSumbangsihToolbar()
        hideActionBar()
        val args = arguments?.getInt("tutor_type") ?: 0

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.apply {

            when (args) {
                1 -> {
                    binding.tvTitleTutor.text = "Apa itu BLT UMKM"
                    binding.labelPageTitleTopbar.text = "Apa Itu BLT"
                    binding.tvContent.text = getString(R.string.content_tutor_1)
                }
                2 -> {
                    binding.tvTitleTutor.text = "Syarat Pengajuan"
                    binding.labelPageTitleTopbar.text = "Syarat Pengajuan"
                    binding.tvContent.text = getString(R.string.content_tutor_2)
                }
                3 -> {
                    binding.tvTitleTutor.text = "Langkah - Langkah"
                    binding.labelPageTitleTopbar.text = "Syarat Pengajuan"
                    binding.tvContent.text = getString(R.string.content_tutor_3)
                }
            }
        }

    }


}