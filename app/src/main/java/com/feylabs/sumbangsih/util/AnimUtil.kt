package com.feylabs.sumbangsih.util

import android.view.animation.Animation

import android.view.animation.Animation.AnimationListener

import android.view.View
import android.view.animation.AnimationUtils
import android.R

object AnimUtil {

    private fun pulse(view: View) {
//        animateFromResource(view, R.anim.pulse_out_repeat)
    }

    private fun fadein(view: View) {
        animateFromResource(view, R.anim.fade_in)
    }

    private fun fadeout(view: View) {
        animateOutFromResource(view, R.anim.fade_out)
    }

    fun animateFromResource(view: View?, animationId: Int) {
        animateInFromResource(view, animationId)
    }

    fun animateInFromResource(view: View?, animationId: Int) {
        animateFromResource(view, animationId, false)
    }

    fun animateOutFromResource(view: View?, animationId: Int) {
        animateFromResource(view, animationId, true)
    }

    fun animateFromResource(view: View?, animationId: Int, out: Boolean) {
        if (view == null) return
        val animation: Animation = AnimationUtils.loadAnimation(view.getContext(), animationId)
        val alpha: Float = view.getAlpha()
        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                if (out) {
                    view.setVisibility(View.GONE)
                    view.setAlpha(alpha)
                }
            }
        })
        if (!out) view.setVisibility(View.VISIBLE)
        view.startAnimation(animation)
    }
}