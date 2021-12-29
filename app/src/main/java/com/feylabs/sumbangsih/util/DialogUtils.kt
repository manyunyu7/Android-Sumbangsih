package com.feylabs.sumbangsih.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.LayoutDialogBinding
import com.feylabs.sumbangsih.databinding.LayoutDialogConfPengajuanBinding
import com.feylabs.sumbangsih.databinding.LayoutDialogNotifikasiBinding
import com.feylabs.sumbangsih.databinding.LayoutDialogSingleButtonImageBinding
import com.feylabs.sumbangsih.util.ImageView.loadImage

object DialogUtils {
    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog

    lateinit var loadingDialog: AlertDialog
    var loadingProgress: String = ""

    fun updateLoadingProgress(progress: String) {
        loadingProgress = progress
    }


    fun showCustomDialog(
        context: Context,
        title: String,
        message: String,
        positiveAction: Pair<String, (() -> Unit)?>,
        negativeAction: Pair<String, (() -> Unit)?>? = null,
        autoDismiss: Boolean = false,
        buttonAllCaps: Boolean = true,
        onDismiss: (() -> Unit)? = null
    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.layout_dialog, null as ViewGroup?, false)
        val binding = LayoutDialogBinding.bind(view)
        binding.tvTitle.text = title
        binding.tvMessage.text = message
        binding.btnPositive.let {
            it.text = positiveAction.first
            it.setOnClickListener {
                dialog.dismiss()
                positiveAction.second?.invoke()
            }
            it.isAllCaps = buttonAllCaps
        }
        negativeAction?.let { pair ->
            binding.btnNegative.let {
                it.visibility = View.VISIBLE
                it.text = pair.first
                it.setOnClickListener {
                    dialog.dismiss()
                    pair.second?.invoke()
                }
                it.isAllCaps = buttonAllCaps
            }
        }
        builder = AlertDialog.Builder(context)
        builder.setOnDismissListener {
            onDismiss?.invoke()
        }
        builder.setView(view)
        builder.setCancelable(autoDismiss)
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    fun showSuccessDialog(
        context: Context,
        title: String,
        message: String,
        positiveAction: Pair<String, (() -> Unit)?>,
        autoDismiss: Boolean = false,
        buttonAllCaps: Boolean = true,
        img: Int = R.drawable.ic_checklist_red
    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.layout_dialog_single_button_image, null as ViewGroup?, false)
        val binding = LayoutDialogSingleButtonImageBinding.bind(view)
        binding.tvTitle.text = title
        binding.tvMessage.text = message
        binding.btnPositive.let {
            it.text = positiveAction.first
            it.setOnClickListener {
                dialog.dismiss()
                positiveAction.second?.invoke()
            }
            it.isAllCaps = buttonAllCaps
        }
        binding.imgLogo.loadImage(context, img)
        builder = AlertDialog.Builder(context)
        builder.setView(view)
        builder.setCancelable(autoDismiss)
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    fun showTosPengajuanDialog(
        context: Context,
        title: String="",
        message: String="",
        negativeAction: Pair<String, (() -> Unit)?>? = null,
        positiveAction: Pair<String, (() -> Unit)?>,
        autoDismiss: Boolean = false,
        buttonAllCaps: Boolean = true,
        img: Int = R.drawable.ic_checklist_red
    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.layout_dialog_conf_pengajuan, null as ViewGroup?, false)
        val binding = LayoutDialogConfPengajuanBinding.bind(view)
//        binding.tvTitle.text = title
//        binding.tvMessage.text = message
        binding.btnPositive.let {
//            it.text = positiveAction.first
            it.setOnClickListener {
                dialog.dismiss()
                positiveAction.second?.invoke()
            }
            it.isAllCaps = buttonAllCaps
        }

        negativeAction?.let { pair ->
            binding.btnNegative.let {
                it.visibility = View.VISIBLE
//                it.text = pair.first
                it.setOnClickListener {
                    dialog.dismiss()
                    pair.second?.invoke()
                }
                it.isAllCaps = buttonAllCaps
            }
        }

        builder = AlertDialog.Builder(context)
        builder.setView(view)
        builder.setCancelable(autoDismiss)
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


    fun showMnotifDialog(
        context: Context,
        title: String,
        subTitle: String,
        message: String,
        positiveAction: Pair<String, (() -> Unit)?>,
        negativeAction: Pair<String, (() -> Unit)?>? = null,
        autoDismiss: Boolean = false,
        buttonAllCaps: Boolean = true,
        onDismiss: (() -> Unit)? = null
    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.layout_dialog_notifikasi, null as ViewGroup?, false)
        val binding = LayoutDialogNotifikasiBinding.bind(view)
        binding.tvTitle.text = title
        binding.tvSubTitle.text=subTitle
        binding.tvMessage.text = message
        binding.btnPositive.let {
            it.text = positiveAction.first
            it.setOnClickListener {
                dialog.dismiss()
                positiveAction.second?.invoke()
            }
            it.isAllCaps = buttonAllCaps
        }
        negativeAction?.let { pair ->
            binding.btnNegative.let {
                it.visibility = View.VISIBLE
                it.text = pair.first
                it.setOnClickListener {
                    dialog.dismiss()
                    pair.second?.invoke()
                }
                it.isAllCaps = buttonAllCaps
            }
        }
        builder = AlertDialog.Builder(context)
        builder.setOnDismissListener {
            onDismiss?.invoke()
        }
        builder.setView(view)
        builder.setCancelable(autoDismiss)
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }




}