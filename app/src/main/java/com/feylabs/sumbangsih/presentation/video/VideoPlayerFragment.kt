package com.feylabs.sumbangsih.presentation.video

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.FragmentVideoPlayerBinding
import com.feylabs.sumbangsih.util.BaseFragment


class VideoPlayerFragment : BaseFragment() {


    var _binding : FragmentVideoPlayerBinding? = null
    val binding get() = _binding as FragmentVideoPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoPlayerBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideActionBar()
        val url = arguments?.getString("url").toString()
        binding.andExoPlayerView.setSource(url)
    }




}