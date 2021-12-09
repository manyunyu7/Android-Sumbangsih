package com.feylabs.sumbangsih.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.SumbangsihApplication.Companion.URL_VIDEO
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.NewsResponse
import com.feylabs.sumbangsih.databinding.FragmentHomeBinding
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {

    val homeViewModel: HomeViewModel by viewModel()
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val adapter by lazy { NewsAdapter() }

    private fun initUI() {
        binding.rvNews.adapter = adapter
        binding.rvNews.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter.newsGridClickAdapter = object : OnNewsGridClick {
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
        homeViewModel.getNews()
    }

    private fun initObserver() {
        homeViewModel.newsLiveData.observe(viewLifecycleOwner, Observer {
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
        })
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