package com.feylabs.sumbangsih.presentation.chat

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.request.StoreChatRequest
import com.feylabs.sumbangsih.databinding.ChatCsFragmentBinding
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import kotlin.concurrent.fixedRateTimer

class ChatCsFragment : BaseFragment() {

    private var _binding: ChatCsFragmentBinding? = null
    private val binding get() = _binding as ChatCsFragmentBinding

    private val mAdapter by lazy { RsChatAdapter() }

    val viewModel: ChatCsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ChatCsFragmentBinding.inflate(inflater)
        return binding.root
    }

    private fun initData() {
        viewModel.fetchChatByUserId(RazPreferenceHelper.getUserId(requireContext()))
    }

    override fun onResume() {
        super.onResume()
        initData()
        showShimmerChat(true)
    }


    private fun initObserver() {
        viewModel.listChatVm.observe(viewLifecycleOwner, {
            when (it) {
                is ManyunyuRes.Default -> {
                    binding.includeEmpty.root.makeViewGone()
                    showShimmerChat(false)
                }
                is ManyunyuRes.Empty -> {
                    binding.includeEmpty.root.makeViewGone()
                    showShimmerChat(false)
                }
                is ManyunyuRes.Error -> {
                    binding.includeEmpty.root.makeViewGone()
                    showShimmerChat(false)
                }

                is ManyunyuRes.Loading -> {
                    binding.includeEmpty.root.makeViewGone()
                    showShimmerChat(false)
                }
                is ManyunyuRes.Success -> {
                    it.data?.resData?.toMutableList()
                        ?.let { chatData ->
                            mAdapter.addData(chatData)
                        }
                    if (mAdapter.itemCount == 0) {
                        binding.includeEmpty.root.makeViewVisible()
                    } else {
                        binding.includeEmpty.root.makeViewGone()
                    }
                    showShimmerChat(false)
                }
            }
        })


        viewModel.saveChatVm.observe(viewLifecycleOwner, {
            when (it) {
                is ManyunyuRes.Default -> {
                    binding.includeEmpty.root.makeViewGone()
                    showShimmerChat(false)
                }
                is ManyunyuRes.Empty -> {
                    binding.includeEmpty.root.makeViewGone()
                    showShimmerChat(false)
                }
                is ManyunyuRes.Error -> {
                    binding.includeEmpty.root.makeViewGone()
                    showShimmerChat(false)
                }

                is ManyunyuRes.Loading -> {
                    showShimmerChat(true)
                }
                is ManyunyuRes.Success -> {
                    binding.etInput.setText("")
                    viewModel.fetchChatByUserId(RazPreferenceHelper.getUserId(requireContext()))
                    showShimmerChat(false)
                }
            }
        })
    }

    private fun initUI() {
        binding.btnRefresh.makeViewGone()
        fixedRateTimer("timer", false, 0L, 10 * 1000) {
            requireActivity().runOnUiThread {
                viewModel.fetchChatByUserId(RazPreferenceHelper.getUserId(requireContext()))
            }
        }

        binding.rvChat.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
            setHasFixedSize(true)
        }

        binding.btnRefresh.setOnClickListener {
            showShimmerChat(true)
            viewModel.fetchChatByUserId(RazPreferenceHelper.getUserId(requireContext()))
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack(R.id.navigation_profileFragment, true)
        }

        binding.btnSend.setOnClickListener {
            showShimmerChat(true)
            mAdapter.notifyDataSetChanged()
            val textSender = binding.etInput.text.toString()
            viewModel.saveChat(
                photo = null,
                message = textSender,
                userId = RazPreferenceHelper.getUserId(requireContext())
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initData()
        initObserver()
    }


    private fun showShimmerChat(b: Boolean) {
        if (b) {
            binding.rvChat.makeViewGone()
            binding.includeLoadingChat.root.makeViewVisible()
        } else {
            binding.rvChat.makeViewVisible()
            binding.includeLoadingChat.root.makeViewGone()
        }
    }

}