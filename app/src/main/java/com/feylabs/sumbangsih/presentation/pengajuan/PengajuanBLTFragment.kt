package com.feylabs.sumbangsih.presentation.pengajuan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.ManyunyuRes
import com.feylabs.sumbangsih.databinding.PengajuanBltFragmentBinding
import com.feylabs.sumbangsih.presentation.pengajuan.history.HistoryTimelineAdapter
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.DialogUtils
import com.feylabs.sumbangsih.util.ImageView.loadImage
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper
import com.feylabs.sumbangsih.util.sharedpref.RazPreferences
import org.koin.android.viewmodel.ext.android.viewModel

class PengajuanBLTFragment : BaseFragment() {


    private var _binding: PengajuanBltFragmentBinding? = null
    private val binding get() = _binding as PengajuanBltFragmentBinding
    private val adapter by lazy { HistoryTimelineAdapter() }

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
        viewModel.fetchHistory(RazPreferenceHelper.getUserId(requireContext()))
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
                    makeSrlLoading(false)
                    showLoading(false)
                }
                is ManyunyuRes.Empty -> {
                    makeSrlLoading(false)
                    showLoading(false)
                }
                is ManyunyuRes.Error -> {
                    makeSrlLoading(false)
                    showLoading(false)
                }
                is ManyunyuRes.Loading -> {
                    makeSrlLoading(true)
                    showLoading(true)
                }
                is ManyunyuRes.Success -> {
                    it.data?.resData?.toMutableList()?.let { data ->
                        adapter.setWholeData(
                            data
                        )

                        val listRole = mutableListOf<String>()
                        val listStatus = mutableListOf<String>()

                        listRole.clear()
                        listStatus.clear()

                        data.forEachIndexed { index, resData ->
                            listRole.add(resData.role)
                            listStatus.add(resData.status)
                        }

                        setupCardStepper(listRole, listStatus)

                    }
                    viewModel.fireHistoryVm()
                    showLoading(false)
                    makeSrlLoading(false)
                }
            }
        })
    }

    private fun setupCardStepper(listRole: MutableList<String>, status: MutableList<String>) {
        when (listRole.first()) {
            "0" -> {
                setupStepper(1)
            }
            "4" -> {
                setupStepper(2)
            }
            "5" -> {
                setupStepper(3)
            }
            else -> {
                //is admin
            }
        }

        when (status.first()) {
            "2" -> {
                // if status is Admin - Proses Seleksi / Pending
                setupStepper(3)
            }
            "3" -> {
                setupStepper(4)
            }
            "10" -> {
                setupStepper(5)
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initData()
        initUi()
    }

    private fun initUi() {

        binding.includeHistory.rvTimeline.let {
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            it.adapter = adapter
            it.setHasFixedSize(true)
        }

        binding.includeHistory.srl.setOnRefreshListener {
            viewModel.fetchHistory(RazPreferenceHelper.getUserId(requireContext()))
        }


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

    private fun setupStepper(counter: Int) {
        binding.includeHistory.apply {

            when (counter) {
                1 -> {
                    includeStepper.apply {
                        val list = arrayOf(card2, card3, card4, card5)
                        val listImage = arrayOf(image2, image3, image4, image5)
                        val listStepper = arrayOf(indicator2, indicator3, indicator4, indicator5)

                        listStepper.forEachIndexed { index, view ->
                            view.background = ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.rectangle_rounded_right
                            )
                            mSetTint(view, R.color.grey_cf)
                        }

                        listImage.forEachIndexed { index, imageView ->
                            imageView.setImageDrawable(null)
                        }

                        list.forEachIndexed { index, linearLayout ->
                            linearLayout.background =
                                ContextCompat.getDrawable(requireContext(), R.color.grey_cf)
                        }

                        mSetTint(indicator1, R.color.redSumbangsih)
                        mSetupCardBackground(card1, R.drawable.gradient_red_stepper)
                        image1.loadImage(requireContext(), R.drawable.ic_step_1_sku)
                    }
                }

                2 -> {
                    includeStepper.apply {
                        val list = arrayOf(card3, card4, card5)
                        val listImage = arrayOf(image3, image4, image5)
                        val listStepper = arrayOf(indicator3, indicator4, indicator5)

                        listStepper.forEachIndexed { index, view ->
                            view.background = ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.rectangle_rounded_right
                            )

                            mSetTint(view, R.color.grey_cf)
                        }


                        listImage.forEachIndexed { index, imageView ->
                            imageView.setImageDrawable(null)
                        }

                        list.forEachIndexed { index, linearLayout ->
                            linearLayout.background =
                                ContextCompat.getDrawable(requireContext(), R.color.grey_cf)
                        }

                        mSetTint(indicator1, R.color.redSumbangsih)
                        mSetTint(indicator2, R.color.redSumbangsih)
                        mSetupCardBackground(card1, R.drawable.gradient_red_stepper)
                        mSetupCardBackground(card2, R.drawable.gradient_red_stepper)
                        image1.loadImage(requireContext(), R.drawable.ic_step_1_sku)
                        image2.loadImage(requireContext(), R.drawable.ic_step_2_sku)
                    }
                }

                3 -> {
                    includeStepper.apply {
                        val list = arrayOf(card4, card5)
                        val listImage = arrayOf(image4, image5)
                        val listStepper = arrayOf(indicator3, indicator4, indicator5)

                        listStepper.forEachIndexed { index, view ->
                            view.background = ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.rectangle_rounded_right
                            )

                            mSetTint(view, R.color.grey_cf)
                        }

                        listImage.forEachIndexed { index, imageView ->
                            imageView.setImageDrawable(null)
                        }

                        list.forEachIndexed { index, linearLayout ->
                            linearLayout.background =
                                ContextCompat.getDrawable(requireContext(), R.color.grey_cf)
                        }

                        mSetTint(indicator1, R.color.redSumbangsih)
                        mSetTint(indicator2, R.color.redSumbangsih)
                        mSetTint(indicator3, R.color.redSumbangsih)
                        mSetupCardBackground(card1, R.drawable.gradient_red_stepper)
                        mSetupCardBackground(card2, R.drawable.gradient_red_stepper)
                        mSetupCardBackground(card3, R.drawable.gradient_red_stepper)
                        image1.loadImage(requireContext(), R.drawable.ic_step_1_sku)
                        image2.loadImage(requireContext(), R.drawable.ic_step_2_sku)
                        image3.loadImage(requireContext(), R.drawable.ic_step_3_sku)
                    }
                }

                4 -> {
                    includeStepper.apply {
                        val list = arrayOf(card5)
                        val listImage = arrayOf(image5)
                        val listStepper = arrayOf(indicator5)

                        listStepper.forEachIndexed { index, view ->
                            view.background = ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.rectangle_rounded_right
                            )
                            mSetTint(view, R.color.grey_cf)
                        }

                        listImage.forEachIndexed { index, imageView ->
                            imageView.setImageDrawable(null)
                        }

                        list.forEachIndexed { index, linearLayout ->
                            linearLayout.background =
                                ContextCompat.getDrawable(requireContext(), R.color.grey_cf)
                        }

                        mSetTint(indicator1, R.color.redSumbangsih)
                        mSetTint(indicator2, R.color.redSumbangsih)
                        mSetTint(indicator3, R.color.redSumbangsih)
                        mSetTint(indicator4, R.color.redSumbangsih)
                        mSetupCardBackground(card1, R.drawable.gradient_red_stepper)
                        mSetupCardBackground(card2, R.drawable.gradient_red_stepper)
                        mSetupCardBackground(card3, R.drawable.gradient_red_stepper)
                        mSetupCardBackground(card4, R.drawable.gradient_red_stepper)
                        image1.loadImage(requireContext(), R.drawable.ic_step_1_sku)
                        image2.loadImage(requireContext(), R.drawable.ic_step_2_sku)
                        image3.loadImage(requireContext(), R.drawable.ic_step_3_sku)
                        image4.loadImage(requireContext(), R.drawable.ic_step_4_sku)
                    }
                }

                5 -> {
                    includeStepper.apply {
                        mSetTint(indicator1, R.color.redSumbangsih)
                        mSetTint(indicator2, R.color.redSumbangsih)
                        mSetTint(indicator3, R.color.redSumbangsih)
                        mSetTint(indicator4, R.color.redSumbangsih)
                        mSetTint(indicator5, R.color.redSumbangsih)
                        mSetupCardBackground(card1, R.drawable.gradient_red_stepper)
                        mSetupCardBackground(card2, R.drawable.gradient_red_stepper)
                        mSetupCardBackground(card3, R.drawable.gradient_red_stepper)
                        mSetupCardBackground(card4, R.drawable.gradient_red_stepper)
                        mSetupCardBackground(card5, R.drawable.gradient_red_stepper)
                        image1.loadImage(requireContext(), R.drawable.ic_step_1_sku)
                        image2.loadImage(requireContext(), R.drawable.ic_step_2_sku)
                        image3.loadImage(requireContext(), R.drawable.ic_step_3_sku)
                        image4.loadImage(requireContext(), R.drawable.ic_step_4_sku)
                        image5.loadImage(requireContext(), R.drawable.ic_step_5_sku)
                    }

                }
            }
        }
    }

    private fun mSetupCardBackground(card: View, res: Int) {
        val sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            card.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), res));
        } else {
            card.background = ContextCompat.getDrawable(requireContext(), res);
        }
    }

    fun mSetTint(view: View, color: Int) {
        view.background.setTint(ContextCompat.getColor(requireContext(), color))
    }

}