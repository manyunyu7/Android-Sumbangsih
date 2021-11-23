package com.feylabs.sumbangsih.presentation._walkthrough

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.FragmentOnBoardBinding


class OnBoardFragment(val orders: Int = 0) : Fragment() {

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
        _binding = FragmentOnBoardBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when(orders){
            1->{
                binding.img.setImageResource(R.drawable.on_boarding_img_1)
            }
            2->{
                binding.img.setImageResource(R.drawable.on_boarding_img_2)
            }
            3->{
                binding.img.setImageResource(R.drawable.on_boarding_img_3)
            }
        }
    }


}