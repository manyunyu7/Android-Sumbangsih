package com.feylabs.sumbangsih.presentation.pengajuan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.databinding.PengajuanBltFragmentBinding
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.DialogUtils
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import com.feylabs.sumbangsih.util.sharedpref.RazPreferences
import org.koin.android.viewmodel.ext.android.viewModel

class PengajuanBLTFragment : BaseFragment() {


    private var _binding: PengajuanBltFragmentBinding? = null
    private val binding get() = _binding as PengajuanBltFragmentBinding

    val viewModel: PengajuanViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PengajuanBltFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        showLoading(false)
        initData()
    }

    fun initData() {
        viewModel.selfCheckEvent(RazPreferenceHelper.getUserId(requireContext()))
        viewModel.activeEvent()
    }

    fun initObserver() {
        viewModel.selfCheckVm.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ManyunyuRes.Default -> {
                    showLoading(false)
                }
                is ManyunyuRes.Empty -> {
                    showLoading(false)
                }
                is ManyunyuRes.Error -> {
                    showLoading(false)
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = "Perhatian",
                        message = "Terjadi Kesalahan ${it.message}, Silakan Coba Kembali Nanti",
                        positiveAction = Pair(getString(R.string.dialog_ok), {
                            findNavController().navigate(R.id.navigation_home)
                        }),
                        autoDismiss = true,
                        onDismiss = {

                        },
                        buttonAllCaps = false
                    )

                }
                is ManyunyuRes.Loading -> {
                    showLoading(true)
                }
                is ManyunyuRes.Success -> {
                    val data = it.data
                    binding.apply {
                        if (data?.apiCode == 3) {
                            includeHistory.includeEmptyHistory.btnAction.makeViewGone()
                            includeActive.tvTitle.text = "Pengajuan BLT UMKM"
                            includeActive.btnAction.makeViewGone()
                            includeActive.tvDesc.text =
                                "Pengajuan anda sedang dalam proses. Silahkan lihat halaman Riwayat. Anda dapat mengajukan komplain mengenai BLT UMKM."
                        }

                        if (data?.apiCode == 4) {
                            includeHistory.includeEmptyHistory.btnAction.makeViewGone()
                            includeActive.btnAction.makeViewGone()
                            includeActive.tvTitle.text = "Maaf, Pengajuan BLT Anda Ditolak"
                            includeActive.tvDesc.text =
                                "Pengajuan anda ditolak, silahkan lihat halaman riwayat untuk tracking status pengajuan Anda"
                        }

                        if (data?.apiCode == 1) {
                            includeHistory.includeEmptyHistory.btnAction.makeViewGone()
                            includeActive.btnAction.makeViewGone()
                            includeActive.tvDesc.text = "Pengajuan BLT Diterima"
                            includeActive.tvDesc.text =
                                "Pengajuan Anda telah diterima, silahkan pergi ke bank yang telah terdaftar untuk mencairkan uang"
                        }

                        if (data?.apiCode == 0) {
                            includeActive.btnAction.makeViewVisible()
                        }
                    }

                    showLoading(false)
                }
            }
        })
        viewModel.activeEventVm.observe(viewLifecycleOwner, {
            when (it) {
                is ManyunyuRes.Default -> {
                    showLoading(false)
                }
                is ManyunyuRes.Empty -> {
                    showLoading(false)
                }
                is ManyunyuRes.Error -> {
                    showLoading(false)
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = "Perhatian",
                        message = "Hallo, Terima kasih sudah melakukan pengajuan BLT, saat ini belum ada kegiatan BLT yang berlangsung di wilayahmu",
                        positiveAction = Pair(getString(R.string.dialog_ok), {
                            findNavController().navigate(R.id.navigation_home)
                            viewModel.fireCheckBLT()
                        }),
                        autoDismiss = true,
                        onDismiss = {
                            viewModel.fireCheckBLT()
                        },
                        buttonAllCaps = false
                    )

                }
                is ManyunyuRes.Loading -> {
                    showLoading(true)
                }
                is ManyunyuRes.Success -> {
                    val data = it.data
                    if (data?.apiCode == 1) {
                        val obj = data.resData?.id
                        if (obj != null) {
                            RazPreferences(requireContext()).save("event_id", obj.toString())
                        } else {
                            findNavController().popBackStack()
                            DialogUtils.showCustomDialog(
                                context = requireContext(),
                                title = "Perhatian",
                                message = "Hallo, Terima kasih sudah melakukan pengajuan BLT, saat ini belum ada kegiatan BLT yang berlangsung di wilayahmu",
                                positiveAction = Pair(getString(R.string.dialog_ok), {}),
                                autoDismiss = true,
                                buttonAllCaps = false
                            )
                        }
                    }
                    showLoading(false)
                }
            }
        })

        viewModel.historyVm.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ManyunyuRes.Default -> {
                    showLoading(false)
                }
                is ManyunyuRes.Empty -> {
                    showLoading(false)
                }
                is ManyunyuRes.Error -> {
                    showLoading(false)
                }
                is ManyunyuRes.Loading -> {
                    showLoading(true)
                }
                is ManyunyuRes.Success -> {
                    showLoading(false)
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initData()


        binding.indicatorBltActive.makeViewVisible()
        binding.indicatorBltHistory.makeViewGone()

        binding.tvActive.setOnClickListener {
            binding.indicatorBltHistory.makeViewGone()
            binding.indicatorBltActive.makeViewVisible()

            binding.includeActive.root.makeViewVisible()
            binding.includeHistory.root.makeViewGone()
        }

        binding.tvHistory.setOnClickListener {
            binding.indicatorBltHistory.makeViewVisible()
            binding.indicatorBltActive.makeViewGone()

            binding.includeActive.root.makeViewGone()
            binding.includeHistory.root.makeViewVisible()
        }

        binding.includeActive.btnAction.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_pengajuanBLTFragment_to_pengajuanBLTStepInitialFragment)
        }

        binding.includeHistory.includeEmptyHistory.btnAction.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_pengajuanBLTFragment_to_pengajuanBLTStepInitialFragment)
        }

    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.includeLoading.root.makeViewVisible()
        } else {
            binding.includeLoading.root.makeViewGone()
        }
    }

}