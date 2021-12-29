package com.feylabs.sumbangsih.presentation.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.data.source.remote.response.MNotificatinRes
import com.feylabs.sumbangsih.data.source.remote.response.NewsResponse
import com.feylabs.sumbangsih.databinding.FragmentNotificationsBinding
import com.feylabs.sumbangsih.databinding.ListAllNewsFragmentBinding
import com.feylabs.sumbangsih.presentation.CommonViewModel
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.CommonHelper.logout
import com.feylabs.sumbangsih.util.DialogUtils
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class NotificationFragment : BaseFragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding as FragmentNotificationsBinding

    private val viewModel: NotificationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initData()
        initObserver()
        makeSrlLoading(true)
    }

    private val adapter by lazy { NotificationAdapter() }

    private fun initUI() {

        binding.srl.setOnRefreshListener {
            viewModel.getNotif(RazPreferenceHelper.getUserId(requireContext()))
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.rv.adapter = adapter
        binding.rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.notifInterface = object : OnNotifCompactClick {

            override fun onclick(model: MNotificatinRes.ResData) {
                DialogUtils.showMnotifDialog(
                    context = requireContext(),
                    title = model.title,
                    subTitle = model.message.toString(),
                    message = model.desc,
                    positiveAction = Pair("Tandai Telah Dibaca", {
                        viewModel.setRead(model.id.toString())
                    }),
                    negativeAction = Pair("Tutup", {}),
                    autoDismiss = true,
                    buttonAllCaps = false
                )
            }
        }
    }

    private fun initData() {
        viewModel.getNotif(RazPreferenceHelper.getUserId(requireContext()))
    }

    private fun initObserver() {
        viewModel.setReadLiveData.observe(viewLifecycleOwner, Observer {
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

                }
                is ManyunyuRes.Success -> {
                    viewModel.getNotif(RazPreferenceHelper.getUserId(requireContext()))
                    viewModel.fireSetRead()
                }
            }

            if (it is ManyunyuRes.Loading) {
                makeSrlLoading(true)
            } else {
                makeSrlLoading(false)
            }

        })

        viewModel.notifLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ManyunyuRes.Default -> {
                }
                is ManyunyuRes.Empty -> {
                }
                is ManyunyuRes.Error -> {
                    showToast(it.message.toString())
                }
                is ManyunyuRes.Loading -> {

                }
                is ManyunyuRes.Success -> {
                    it.data?.resData?.let { data ->
                        adapter.setWholeData(data.toMutableList())
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