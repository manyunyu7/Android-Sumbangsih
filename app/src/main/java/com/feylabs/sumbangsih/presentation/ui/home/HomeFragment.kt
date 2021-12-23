package com.feylabs.sumbangsih.presentation.ui.home

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
import com.feylabs.sumbangsih.data.source.local.NewsSeeder
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.NewsResponse
import com.feylabs.sumbangsih.data.source.remote.response.ProfileMainReq
import com.feylabs.sumbangsih.databinding.FragmentHomeBinding
import com.feylabs.sumbangsih.presentation.CommonControllerActivity
import com.feylabs.sumbangsih.util.AnimUtil.animateFromResource
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {

    //    val homeViewModel: CommonViewModel by sharedViewModel<CommonViewModel>()
    val homeViewModel: HomeViewModel by viewModel()
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding as FragmentHomeBinding

    private val mAdapter by lazy { NewsAdapter() }

    private fun initUI() {

        setupAnimHome()
        setCardStatsLoading(true)

        binding.tvMenuNews.setOnClickListener {
            homeViewModel.getNews()
        }

        binding.tvNewsAll.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_listAllNewsFragment)
        }
        binding.btnNewsAll.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_listAllNewsFragment)
        }

        binding.profileCard.setOnClickListener {
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
            homeViewModel.getStatsData()
            homeViewModel.getNews()
            homeViewModel.getProfile(RazPreferenceHelper.getUserId(requireContext()))
        }
    }

    private fun initObserver() {

        homeViewModel.statsLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ManyunyuRes.Default -> {
                    setCardStatsLoading(false)
                }
                is ManyunyuRes.Empty -> {
                    setCardStatsLoading(false)
                }
                is ManyunyuRes.Error -> {
                    setCardStatsLoading(false)
                }
                is ManyunyuRes.Loading -> {
                    setCardStatsLoading(true)
                }
                is ManyunyuRes.Success -> {
                    setCardStatsLoading(false)
                    val data = it.data
                    data?.let {
                        binding.includeHomeStatistics.apply {
                            tvPenerima.text = data.resData.penerima.toString()
                            tvPengajuan.text = data.resData.pengajuan.toString()
                            tvSisaKuota.text = data.resData.sisaKuota.toString()
                        }
                    }
                }
            }
        })

        homeViewModel.profileLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ManyunyuRes.Default -> {
                    setProfileLoading(false)
                }
                is ManyunyuRes.Empty -> {
                    setProfileLoading(false)
                }
                is ManyunyuRes.Error -> {
                    setProfileLoading(false)
                }
                is ManyunyuRes.Loading -> {
                    setProfileLoading(true)
                }
                is ManyunyuRes.Success -> {
                    val data = it.data
                    if (data != null) {
                        setupProfileCard(data)
                    }
                    setProfileLoading(false)
                }
            }
        })

        homeViewModel.newsLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ManyunyuRes.Default -> {
                    setNewsLoading(false)
                }
                is ManyunyuRes.Empty -> {
                    setNewsLoading(false)
                    mAdapter.setWholeData(NewsSeeder.getNewsSeeder())
                    showToast("Tidak Ada Data")
                }
                is ManyunyuRes.Error -> {
                    setNewsLoading(false)
                    mAdapter.setWholeData(NewsSeeder.getNewsSeeder())
                    showToast(it.message.toString())
                }
                is ManyunyuRes.Loading -> {
                    setNewsLoading(true)
                }
                is ManyunyuRes.Success -> {
                    setNewsLoading(false)
                    val isNewsEmpty = it?.data?.isEmpty() ?: true

                    it.data?.toMutableList()?.let { data ->
                        mAdapter.setWholeData(data)
                    }

                    // if news above is empty
                    if (isNewsEmpty) {
                        mAdapter.setWholeData(NewsSeeder.getNewsSeeder())
                    }
                }
            }
        })
    }


    private fun setupProfileCard(data: ProfileMainReq) {
        val mKtp = data.resData.ktp
        val user = data.resData.user

        if (mKtp == null) {
            binding.tvSapa.text = "Halo, +" + RazPreferenceHelper.getPhoneNumber(requireContext())
            binding.tvDesc.text = "Segera lakukan verifikasi NIK"

            (getActivity() as CommonControllerActivity).overrideBottomMenu(
                true, "Perhatian", "Silakan Melakukan Verifikasi NIK terlebih dahulu"
            )

        } else {
            binding.profileCard.setOnClickListener {
                findNavController().navigate(R.id.navigation_profileFragment)
            }
            binding.tvSapa.text = "Halo, " + mKtp.name
            binding.tvDesc.text = "NIK : " + mKtp.nik

            if (mKtp.verificationStatus == null) {
                val messageShown = "Pengajuan NIK Anda belum disetujui, "

                (getActivity() as CommonControllerActivity).overrideBottomMenu(
                    true, "Perhatian", messageShown
                )
            }

            if (mKtp.verificationStatus == 0) {
                var messageShown = "Pengajuan NIK Anda tidak memenuhi persyaratan, "
                var messageFromServer = mKtp.verificationNotes

                if (messageFromServer != null) {
                    messageShown += "dengan catatan :\n $messageFromServer. \n\nSilakan Melakukan Verifikasi NIK Kembali"
                }

                binding.profileCard.setOnClickListener {
                    findNavController().navigate(R.id.action_navigation_home_to_KTPVerifStep1Fragment)
                }

                (getActivity() as CommonControllerActivity).overrideBottomMenu(
                    true, "Perhatian", messageShown
                )
            }

            if (mKtp.verificationStatus == 1) {
                (getActivity() as CommonControllerActivity).overrideBottomMenu(
                    false
                )
            }

        }

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


    private fun setCardStatsLoading(b: Boolean) {
        if (b) {
            binding.shimmerStatsCard.root.makeViewVisible()
            binding.includeHomeStatistics.root.makeViewGone()
        } else {
            binding.shimmerStatsCard.root.makeViewGone()
            binding.includeHomeStatistics.root.makeViewVisible()
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
        binding.tvSapa.text = "Halo, +" + number
        binding.tvDesc.text = "Segera lakukan verifikasi NIK"

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

    private fun setProfileLoading(value: Boolean) {
        if (value) {
            binding.shimmerProfileCard.root.makeViewVisible()
            binding.profileCard.makeViewGone()
        } else {
            binding.shimmerProfileCard.root.makeViewGone()
            binding.profileCard.makeViewVisible()
        }
    }


    private fun setupAnimHome() {
        binding.apply {
            //includeHomeStatistics.root.makeViewGone()
            includeHomeTutor.root.makeViewGone()
            btnTutorialVideo.makeViewGone()
            tvVideo.makeViewGone()
            tvTutor.makeViewGone()
            containerNews.makeViewGone()
            containerProfile.makeViewGone()

            animateFromResource(containerProfile, R.anim.anim_slide_in_right)
            animateFromResource(containerNews, R.anim.anim_slide_in_left)
            //animateFromResource(binding.includeHomeStatistics.root, R.anim.anim_slide_in_right)
            animateFromResource(includeHomeTutor.root, R.anim.anim_slide_in_left)
            animateFromResource(tvTutor, R.anim.anim_slide_in_right)
            animateFromResource(tvVideo, R.anim.anim_slide_in_left)
            animateFromResource(btnTutorialVideo, R.anim.anim_slide_in_left)

            containerProfile.makeViewVisible()
            tvVideo.makeViewVisible()
            tvTutor.makeViewVisible()
            btnTutorialVideo.makeViewVisible()
            //includeHomeStatistics.root.makeViewVisible()
            includeHomeTutor.root.makeViewVisible()
            containerNews.makeViewVisible()
        }
    }

}