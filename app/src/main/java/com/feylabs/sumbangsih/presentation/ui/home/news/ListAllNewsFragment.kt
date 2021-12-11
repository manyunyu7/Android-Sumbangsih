package com.feylabs.sumbangsih.presentation.ui.home.news

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.NewsResponse
import com.feylabs.sumbangsih.databinding.FragmentDetailNewsBinding
import com.feylabs.sumbangsih.databinding.ListAllNewsFragmentBinding
import com.feylabs.sumbangsih.presentation.ui.home.HomeViewModel
import com.feylabs.sumbangsih.presentation.ui.home.NewsAdapter
import com.feylabs.sumbangsih.presentation.ui.home.OnNewsGridClick
import com.feylabs.sumbangsih.util.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class ListAllNewsFragment : BaseFragment() {


    private var _binding: ListAllNewsFragmentBinding? = null
    private val binding get() = _binding as ListAllNewsFragmentBinding

    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ListAllNewsFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initData()
        initObserver()
        makeSrlLoading(true)
    }

    private val adapter by lazy { NewsCompactAdapter() }

    private fun initUI() {

        binding.srl.setOnRefreshListener {
            viewModel.getNews()
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.rvNews.adapter = adapter
        binding.rvNews.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.newsGridClickAdapter = object : OnNewsCompactClick {
            override fun onclick(model: NewsResponse.NewsResponseItem) {
                findNavController().navigate(
                    R.id.navigation_detailNewsFragment, bundleOf(
                        "data" to model
                    )
                )
            }
        }
    }


    private fun initData() {
        viewModel.getNews()
    }

    private fun initObserver() {
        viewModel.newsLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ManyunyuRes.Default -> {
                }
                is ManyunyuRes.Empty -> {
                    showToast("Tidak Ada Data")
                }
                is ManyunyuRes.Error -> {
                    showToast(it.message.toString())
                }
                is ManyunyuRes.Loading -> {
                    showToast("Memuat Berita")
                }
                is ManyunyuRes.Success -> {
                    it.data?.toMutableList()?.let { data ->
                        adapter.setWholeData(data)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            if (it is ManyunyuRes.Loading) {
                makeSrlLoading(true)
            } else {
                makeSrlLoading(false)
            }

        })
    }


}