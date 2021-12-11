package com.feylabs.sumbangsih.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.databinding.LayoutDialogBinding
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

    fun dismissLoadingDialog() {
        if (loadingDialog.isShowing)
            loadingDialog.dismiss()
    }

    fun showCustomDialog(
        context: Context,
        title: String,
        message: String,
        positiveAction: Pair<String, (() -> Unit)?>,
        negativeAction: Pair<String, (() -> Unit)?>? = null,
        autoDismiss: Boolean = false,
        buttonAllCaps: Boolean = true,
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
        builder.setView(view)
        builder.setCancelable(autoDismiss)
        dialog = builder.create()
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
        dialog.show()
    }



}