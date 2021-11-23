package com.feylabs.sumbangsih.presentation._walkthrough

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.FragmentOnBoardBinding


class OnBoardFragment(orders: Int = 0) : Fragment() {

    private var _binding: FragmentOnBoardBinding? = null
    private val binding get() = _binding as FragmentOnBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = FragmentOnBoardBinding.inflate(inflater)
        return view.root
    }


}