package com.feylabs.sumbangsih.presentation.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.SumbangsihApplication.Companion.URL_VIDEO
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.NewsResponse
import com.feylabs.sumbangsih.databinding.FragmentHomeBinding
import com.feylabs.sumbangsih.presentation.CommonViewModel
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {

    //    val homeViewModel: CommonViewModel by sharedViewModel<CommonViewModel>()
    val homeViewModel: HomeViewModel by viewModel()
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding as FragmentHomeBinding

    private val mAdapter by lazy { NewsAdapter() }

    private fun initUI() {
        binding.tvMenuNews.setOnClickListener {
            homeViewModel.getNews()
        }

        binding.tvNewsAll.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_listAllNewsFragment)
        }
        binding.btnNewsAll.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_listAllNewsFragment)
        }

        binding.btnVerifNikCard.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_KTPVerifStep1Fragment)
        }

        binding.rvNews.adapter = mAdapter
        binding.rvNews.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mAdapter.newsGridClickAdapter = object : OnNewsGridClick {
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
        uiScope.launch(Dispatchers.IO) {
            homeViewModel.getNews()
        }
    }

    private fun initObserver() {
        homeViewModel.newsLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ManyunyuRes.Default -> {
                    setNewsLoading(false)
                }
                is ManyunyuRes.Empty -> {
                    setNewsLoading(false)
                    showToast("Tidak Ada Data")
                }
                is ManyunyuRes.Error -> {
                    setNewsLoading(false)
                    binding.tvMenuNews.text = it.message
                    showToast(it.message.toString())
                }
                is ManyunyuRes.Loading -> {
                    setNewsLoading(true)
                }
                is ManyunyuRes.Success -> {
                    setNewsLoading(false)
                    it.data?.toMutableList()?.let { data ->
                        mAdapter.setWholeData(data)
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    private fun setNewsLoading(b: Boolean) {
        if (b) {
            binding.rvNewsPlaceholder.root.makeViewVisible()
            binding.rvNews.makeViewGone()
        } else {
            binding.rvNews.makeViewVisible()
            binding.rvNewsPlaceholder.root.makeViewGone()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideActionBar()
        initUI()
        initData()
        initObserver()

        binding.btnTutorialVideo.setOnClickListener {
            findNavController().navigate(
                R.id.navigation_videoPlayerFragment,
                bundleOf("url" to URL_VIDEO)
            )
        }

        val number = RazPreferenceHelper.getPhoneNumber(requireContext())
        binding.tvSapa.text = number

        binding.includeHomeTutor.apply {
            btnTutorBlt.setOnClickListener {
                goToDetailTutorFragment(1)
            }

            btnTutorSuratPengajuan.setOnClickListener {
                goToDetailTutorFragment(2)
            }

            btnTutorStep.setOnClickListener {
                goToDetailTutorFragment(3)
            }
        }
    }

    private fun goToDetailTutorFragment(tutorType: Int) {
        findNavController().navigate(
            R.id.action_navigation_home_to_navigation_detailTutorialFragment,
            bundleOf("tutor_type" to tutorType)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}