package com.feylabs.sumbangsih.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
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
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Base64
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.io.ByteArrayOutputStream
import java.lang.IllegalArgumentException


private const val placeholderImage: Int = R.drawable.exo_styled_controls_vr

object ImageView {
    fun ImageView.loadImage(
        url: String,
        isSaveCache: Boolean = true,
        placeholder: Int = placeholderImage
    ) {
        Glide.with(this)
            .load(url)
            .thumbnail(Glide.with(context).load(R.raw.ic_loading_google).fitCenter())
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

    fun convertViewToBase64(view: View, height: Int, width: Int): String? {
        val bitmap = getBitmapFromView(view, height = height, width = width)
        if (bitmap != null) {
            val base64 = convertBitmapToBase64(bitmap)
            return base64
        }
        throw error("Bitmap is Null")
    }

    fun convertBitmapToBase64(bitmap: Bitmap): String? {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    fun getBitmapFromView(view: View, width: Int, height: Int): Bitmap {
        // Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        // Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        // Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) {
            // has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas)
        } else {
            // does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        }
        // draw the view on the canvas
        view.draw(canvas)
        // return the bitmap
        return returnedBitmap
    }


    fun getBitmapFromView(view: View): Bitmap? {
        var bitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun getBitmapFromView(view: View, defaultColor: Int): Bitmap? {
        var bitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        canvas.drawColor(defaultColor)
        view.draw(canvas)
        return bitmap
    }

    fun convertImageToStringForServer(imageBitmap: Bitmap?): String? {
        val stream = ByteArrayOutputStream()
        return if (imageBitmap != null) {
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 60, stream)
            val byteArray = stream.toByteArray()
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } else {
            null
        }
    }

    fun getBitmapQRfromString(content: String, width: Int = 512, height: Int = 512): Bitmap {
        val hintMap = mapOf(EncodeHintType.MARGIN to 0)
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hintMap)
        val mWidth = bitMatrix.width
        val mHeight = bitMatrix.height
        val bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565)
        for (x in 0 until mWidth) {
            for (y in 0 until mHeight) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }

        return bitmap
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
