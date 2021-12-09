package com.feylabs.sumbangsih.util

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.feylabs.sumbangsih.R
import timber.log.Timber

private const val placeholderImage: Int = R.drawable.exo_styled_controls_vr

object ImageView {
    fun ImageView.loadImage(
        url: String,
        isSaveCache: Boolean = true,
        placeholder: Int = placeholderImage
    ) {
        Glide.with(this)
            .load(url)
            .apply(
                if (isSaveCache) requestOptionStandart(placeholder) else requestOptionStandartNoSaveCache(
                    placeholder
                )
            )
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Timber.d("NRY_GLIDE $e")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

            })
            .into(this)
    }


    @SuppressLint("CheckResult")
    fun requestOptionStandart(placeholder: Int = placeholderImage): RequestOptions {
        val requestOptions = RequestOptions()
        requestOptions.placeholder(placeholder)
        return requestOptions
    }

    @SuppressLint("CheckResult")
    fun requestOptionStandartNoSaveCache(placeholder: Int = placeholderImage): RequestOptions {
        val requestOptions = RequestOptions()
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
        requestOptions.skipMemoryCache(true)
        requestOptions.placeholder(placeholder)
        return requestOptions
    }
}
