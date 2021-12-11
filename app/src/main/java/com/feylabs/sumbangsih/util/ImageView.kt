package com.feylabs.sumbangsih.util

import android.annotation.SuppressLint
import android.content.Context
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

    fun ImageView.loadImage(
        context: Context,
        drawable: Int,
//        thumbnailsType: ThumbnailsType = ThumbnailsType.LOADING_1
    ) {
        Glide.with(context)
            .load(drawable)
//            .placeholder(thumbnailsType.value)
            .thumbnail(Glide.with(context).load(R.raw.ic_loading_google).fitCenter())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
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
